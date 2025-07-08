package br.com.fiap.tech_challenge_2.service.impl;

import br.com.fiap.tech_challenge_2.application.service.impl.UsuarioServiceImpl;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.infrastructure.repository.UsuarioRepository;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordHasher passwordHasher;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private final EnderecoMapper enderecoMapper = EnderecoMapper.INSTANCE;

    private Endereco createTestEndereco() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("12345678");
        return endereco;
    }

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void save_deveSalvarUsuarioComSucesso() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", Perfil.CLIENTE, "ana", "senha", enderecoDTO);

        Usuario usuario = new Usuario();
        usuario.setLogin("ana");
        usuario.setSenha("senhaHash");

        Usuario saved = new Usuario();
        UsuarioResponse response = mock(UsuarioResponse.class);

        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.empty());
        when(usuarioMapper.toEntity(request)).thenReturn(usuario);
        when(passwordHasher.hashPassword("senha")).thenReturn("senhaHash");
        when(usuarioRepository.save(usuario)).thenReturn(saved);
        when(usuarioMapper.toResponse(saved)).thenReturn(response);

        UsuarioResponse result = usuarioService.save(request);

        assertEquals(response, result);
        verify(usuarioRepository).findByLogin("ana");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void save_deveLancarExcecaoQuandoLoginDuplicado() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", Perfil.CLIENTE, "ana", "Rua 4", enderecoDTO);
        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(new Usuario()));

        assertThrows(DuplicateResourceException.class, () -> usuarioService.save(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void findAll_deveRetornarSetDeUsuarioResponse() {
        Usuario usuario = new Usuario();
        List<Usuario> usuarios = List.of(usuario);
        UsuarioResponse response = mock(UsuarioResponse.class);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioMapper.toResponse(usuario)).thenReturn(response);

        Set<UsuarioResponse> result = usuarioService.findAll();

        assertEquals(1, result.size());
        assertTrue(result.contains(response));
    }

    @Test
    void findById_deveRetornarUsuarioResponse() {
        Usuario usuario = new Usuario();
        UsuarioResponse response = mock(UsuarioResponse.class);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(response);

        UsuarioResponse result = usuarioService.findById(1L);

        assertEquals(response, result);
    }

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findById(1L));
    }

    @Test
    void authenticate_deveAutenticarComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaHash");
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("ana", "senha");

        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("senha", "senhaHash")).thenReturn(true);

        assertTrue(usuarioService.authenticate(loginRequest));
    }

    @Test
    void authenticate_deveLancarExcecaoQuandoLoginNaoEncontrado() {
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("ana", "senha");
        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> usuarioService.authenticate(loginRequest));
    }

    @Test
    void authenticate_deveLancarExcecaoQuandoSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaHash");
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("ana", "senha");

        when(usuarioRepository.findByLogin("ana")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("senha", "senhaHash")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> usuarioService.authenticate(loginRequest));
    }

    @Test
    void update_deveAtualizarUsuarioComSucesso() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioEditRequest request = new UsuarioEditRequest("NovoNome", "novo@email.com", Perfil.CLIENTE, "novaSenha", enderecoDTO);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("AntigoNome");
        usuario.setEmail("antigo@email.com");
        usuario.setEndereco(createTestEndereco());
        usuario.setPerfil("CLIENTE");
        Usuario updated = new Usuario();
        UsuarioResponse response = mock(UsuarioResponse.class);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordHasher.hashPassword("novaSenha")).thenReturn("novaSenhaHash");
        when(usuarioRepository.save(usuario)).thenReturn(updated);
        when(usuarioMapper.toResponse(updated)).thenReturn(response);

        UsuarioResponse result = usuarioService.update(1L, request);

        assertEquals(response, result);
        assertEquals("NovoNome", usuario.getNome());
        assertEquals("novo@email.com", usuario.getEmail());
        assertEquals("Rua Teste", usuario.getEndereco().getLogradouro());
        assertEquals("123", usuario.getEndereco().getNumero());
        assertEquals("Apto 101", usuario.getEndereco().getComplemento());
        assertEquals("SP", usuario.getEndereco().getEstado());
        assertEquals("Centro", usuario.getEndereco().getBairro());
        assertEquals("12345678", usuario.getEndereco().getCep());
        assertEquals("São Paulo", usuario.getEndereco().getCidade());
        assertEquals("CLIENTE", usuario.getPerfil());
        assertEquals("novaSenhaHash", usuario.getSenha());
    }

    @Test
    void update_deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioEditRequest request = new UsuarioEditRequest("Nome", "email", Perfil.PROPRIETARIO, "Endereco", enderecoDTO);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.update(1L, request));
    }

    @Test
    void delete_deveRemoverUsuarioComSucesso() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.delete(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void delete_deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.delete(1L));
    }
}