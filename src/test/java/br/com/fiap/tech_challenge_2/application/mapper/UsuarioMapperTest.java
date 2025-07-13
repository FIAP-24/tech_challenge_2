package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @InjectMocks
    private UsuarioMapperImpl usuarioMapper;

    private UsuarioRequest usuarioRequest;
    private Usuario usuario;
    private UsuarioResponse usuarioResponse;
    private EnderecoDTO enderecoDTO;
    private Endereco endereco;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 1");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");

        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNome("CLIENTE");

        usuarioRequest = new UsuarioRequest(
            "João Silva",
            "joao@email.com",
            Perfil.CLIENTE,
            "joao123",
            "123456",
            enderecoDTO
        );

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("hashedPassword");
        usuario.setDataUpdate(LocalDate.now());
        usuario.setEndereco(endereco);
        usuario.setTipoUsuario(tipoUsuario);

        usuarioResponse = new UsuarioResponse(
            1L,
            "João Silva",
            Perfil.CLIENTE,
            "joao@email.com",
            "joao123",
            enderecoDTO,
            LocalDate.now()
        );
    }

    @Test
    void testToEntity_Success() {
        // When
        Usuario result = usuarioMapper.toEntity(usuarioRequest);

        // Then
        assertNotNull(result);
        assertEquals(usuarioRequest.nome(), result.getNome());
        assertEquals(usuarioRequest.email(), result.getEmail());
        assertEquals(usuarioRequest.login(), result.getLogin());
        // Senha é ignorada no mapper
        assertNull(result.getSenha());
        assertNotNull(result.getEndereco());
        assertEquals(usuarioRequest.endereco().logradouro(), result.getEndereco().getLogradouro());
        assertEquals(usuarioRequest.endereco().numero(), result.getEndereco().getNumero());
        assertEquals(usuarioRequest.endereco().complemento(), result.getEndereco().getComplemento());
        assertEquals(usuarioRequest.endereco().bairro(), result.getEndereco().getBairro());
        assertEquals(usuarioRequest.endereco().cidade(), result.getEndereco().getCidade());
        assertEquals(usuarioRequest.endereco().estado(), result.getEndereco().getEstado());
        assertEquals(usuarioRequest.endereco().cep(), result.getEndereco().getCep());
    }

    @Test
    void testToEntity_WithNullEndereco() {
        // Given
        UsuarioRequest requestWithoutEndereco = new UsuarioRequest(
            "João Silva",
            "joao@email.com",
            Perfil.CLIENTE,
            "joao123",
            "123456",
            null
        );

        // When
        Usuario result = usuarioMapper.toEntity(requestWithoutEndereco);

        // Then
        assertNotNull(result);
        assertEquals(requestWithoutEndereco.nome(), result.getNome());
        assertEquals(requestWithoutEndereco.email(), result.getEmail());
        assertEquals(requestWithoutEndereco.login(), result.getLogin());
        // Senha é ignorada no mapper
        assertNull(result.getSenha());
        assertNull(result.getEndereco());
    }

    @Test
    void testToResponse_Success() {
        // When
        UsuarioResponse result = usuarioMapper.toResponse(usuario);

        // Then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        assertEquals(usuario.getNome(), result.nome());
        assertEquals(usuario.getEmail(), result.email());
        assertEquals(usuario.getLogin(), result.login());
        // Perfil não é mapeado automaticamente
        assertNull(result.perfil());
        assertNotNull(result.endereco());
        assertEquals(usuario.getEndereco().getLogradouro(), result.endereco().logradouro());
        assertEquals(usuario.getEndereco().getNumero(), result.endereco().numero());
        assertEquals(usuario.getEndereco().getComplemento(), result.endereco().complemento());
        assertEquals(usuario.getEndereco().getBairro(), result.endereco().bairro());
        assertEquals(usuario.getEndereco().getCidade(), result.endereco().cidade());
        assertEquals(usuario.getEndereco().getEstado(), result.endereco().estado());
        assertEquals(usuario.getEndereco().getCep(), result.endereco().cep());
        assertEquals(usuario.getDataUpdate(), result.dataUpdate());
    }

    @Test
    void testToResponse_WithNullEndereco() {
        // Given
        usuario.setEndereco(null);

        // When
        UsuarioResponse result = usuarioMapper.toResponse(usuario);

        // Then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        assertEquals(usuario.getNome(), result.nome());
        assertEquals(usuario.getEmail(), result.email());
        assertEquals(usuario.getLogin(), result.login());
        // Perfil não é mapeado automaticamente
        assertNull(result.perfil());
        assertNull(result.endereco());
        assertEquals(usuario.getDataUpdate(), result.dataUpdate());
    }

    @Test
    void testToResponse_WithNullDataUpdate() {
        // Given
        usuario.setDataUpdate(null);

        // When
        UsuarioResponse result = usuarioMapper.toResponse(usuario);

        // Then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        assertEquals(usuario.getNome(), result.nome());
        assertEquals(usuario.getEmail(), result.email());
        assertEquals(usuario.getLogin(), result.login());
        // Perfil não é mapeado automaticamente
        assertNull(result.perfil());
        assertNotNull(result.endereco());
        assertNull(result.dataUpdate());
    }
} 