package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @InjectMocks
    private UsuarioMapperImpl usuarioMapper;

    private Usuario domainUsuario;
    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;
    private Endereco domainEndereco;
    private EnderecoDTO enderecoDTO;
    private TipoUsuario domainTipoUsuario;
    private TipoUsuarioDTO tipoUsuarioDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        
        domainEndereco = new Endereco(1L, "Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        domainTipoUsuario = new TipoUsuario(1L, "CLIENTE");
        
        domainUsuario = new Usuario(1L, "João Silva", "joao@email.com", "joao123", "senha123", LocalDate.now(), domainEndereco, domainTipoUsuario);
        
        usuarioRequest = new UsuarioRequest("João Silva", "joao@email.com", 1l, "joao123", "senha123", enderecoDTO);
        
        usuarioResponse = new UsuarioResponse(1L, "João Silva", tipoUsuarioDTO, "joao@email.com", "joao123", enderecoDTO, LocalDate.now());
    }

    @Test
    void testToResponse_Success() {
        // When
        UsuarioResponse result = usuarioMapper.toResponse(domainUsuario);

        // Then
        assertNotNull(result);
        assertEquals(domainUsuario.getId(), result.id());
        assertEquals(domainUsuario.getNome(), result.nome());
        assertEquals(domainUsuario.getEmail(), result.email());
        assertEquals(domainUsuario.getLogin(), result.login());
        assertEquals(domainUsuario.getDataUpdate(), result.dataUpdate());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.tipoUsuario().nome());
        
        // Endereco é mapeado automaticamente
        assertNotNull(result.endereco());
        assertEquals(domainEndereco.getLogradouro(), result.endereco().logradouro());
    }

    @Test
    void testToResponse_WithNullUsuario() {
        // When
        UsuarioResponse result = usuarioMapper.toResponse(null);

        // Then
        assertNull(result);
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
        // O campo tipoUsuario será null após o mapeamento
        assertNull(result.getTipoUsuario());
        assertNotNull(result.getDataUpdate());
        // Senha não é mapeada automaticamente
        assertNull(result.getSenha());
        // Endereco é mapeado automaticamente
        assertNotNull(result.getEndereco());
        assertEquals(enderecoDTO.logradouro(), result.getEndereco().getLogradouro());
    }

    @Test
    void testToEntity_WithNullRequest() {
        // When
        Usuario result = usuarioMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_WithId() {
        // When
        Usuario result = usuarioMapper.toEntity(usuarioRequest, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(usuarioRequest.nome(), result.getNome());
        assertEquals(usuarioRequest.email(), result.getEmail());
        assertEquals(usuarioRequest.login(), result.getLogin());
        // O campo tipoUsuario será null após o mapeamento
        assertNull(result.getTipoUsuario());
        assertNotNull(result.getDataUpdate());
        // Senha não é mapeada automaticamente
        assertNull(result.getSenha());
    }

    @Test
    void testToEntity_WithIdAndHashedPassword() {
        // When
        Usuario result = usuarioMapper.toEntity(usuarioRequest, 1L, "hashedPassword");

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("hashedPassword", result.getSenha());
        assertEquals(usuarioRequest.nome(), result.getNome());
        assertEquals(usuarioRequest.email(), result.getEmail());
        assertEquals(usuarioRequest.login(), result.getLogin());
        // O campo tipoUsuario será null após o mapeamento
        assertNull(result.getTipoUsuario());
    }

    @Test
    void testToResponseList_Success() {
        // Given
        List<Usuario> domainUsuarios = List.of(domainUsuario);

        // When
        List<UsuarioResponse> result = usuarioMapper.toResponseList(domainUsuarios);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domainUsuario.getId(), result.get(0).id());
        assertEquals(domainUsuario.getNome(), result.get(0).nome());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.get(0).tipoUsuario().nome());
    }

    @Test
    void testToResponseSet_Success() {
        // Given
        Set<Usuario> domainUsuarios = Set.of(domainUsuario);

        // When
        Set<UsuarioResponse> result = usuarioMapper.toResponseSet(domainUsuarios);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domainUsuario.getId(), result.iterator().next().id());
        assertEquals(domainUsuario.getNome(), result.iterator().next().nome());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.iterator().next().tipoUsuario().nome());
    }
} 