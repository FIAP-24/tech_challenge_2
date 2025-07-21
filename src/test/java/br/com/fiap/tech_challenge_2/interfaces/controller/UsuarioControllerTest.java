package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.*;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EntityScan(basePackages = "br.com.fiap.tech_challenge_2.infrastructure.persistence.entity")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUsuarioUseCase createUsuarioUseCase;

    @MockBean
    private FindUsuarioUseCase findUsuarioUseCase;

    @MockBean
    private UpdateUsuarioUseCase updateUsuarioUseCase;

    @MockBean
    private DeleteUsuarioUseCase deleteUsuarioUseCase;

    @MockBean
    private AuthenticateUsuarioUseCase authenticateUsuarioUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;
    private EnderecoDTO enderecoDTO;
    private TipoUsuarioDTO tipoUsuarioDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");

        usuarioRequest = new UsuarioRequest(
                "João Silva",
                "joao@email.com",
                1l,
                "joao123",
                "123456",
                enderecoDTO
        );

        usuarioResponse = new UsuarioResponse(
                1L,
                "João Silva",
                tipoUsuarioDTO,
                "joao@email.com",
                "joao123",
                enderecoDTO,
                LocalDate.now()
        );
    }

    @Test
    void testCreateUsuario_Success() throws Exception {
        // Given
        when(createUsuarioUseCase.execute(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.data.login").value("joao123"))
                .andExpect(jsonPath("$.data.tipoUsuario.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(createUsuarioUseCase).execute(any(UsuarioRequest.class));
    }

    @Test
    void testCreateUsuario_DuplicateLogin() throws Exception {
        // Given
        when(createUsuarioUseCase.execute(any(UsuarioRequest.class)))
                .thenThrow(new DuplicateResourceException("Login já está em uso"));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Login já está em uso"));

        verify(createUsuarioUseCase).execute(any(UsuarioRequest.class));
    }

    @Test
    void testCreateUsuario_InvalidRequest() throws Exception {
        // Given - Invalid request without required fields
        UsuarioRequest invalidRequest = new UsuarioRequest(
                "", // empty nome
                "invalid-email", // invalid email
                null, // null tipoUsuario
                "", // empty login
                "123", // short password
                null // null endereco
        );

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createUsuarioUseCase, never()).execute(any());
    }

    @Test
    void testFindUsuarioById_Success() throws Exception {
        // Given
        when(findUsuarioUseCase.findById(1L)).thenReturn(usuarioResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.data.login").value("joao123"))
                .andExpect(jsonPath("$.data.tipoUsuario.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findUsuarioUseCase).findById(1L);
    }

    @Test
    void testFindUsuarioById_NotFound() throws Exception {
        // Given
        when(findUsuarioUseCase.findById(999L))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado com id: 999"));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com id: 999"));

        verify(findUsuarioUseCase).findById(999L);
    }

    @Test
    void testFindAllUsuarios_Success() throws Exception {
        // Given
        Set<UsuarioResponse> usuarios = Set.of(usuarioResponse);
        when(findUsuarioUseCase.findAll()).thenReturn(usuarios);

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("João Silva"))
                .andExpect(jsonPath("$.data[0].email").value("joao@email.com"))
                .andExpect(jsonPath("$.data[0].login").value("joao123"))
                .andExpect(jsonPath("$.data[0].tipoUsuario.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findUsuarioUseCase).findAll();
    }

    @Test
    void testFindAllUsuarios_EmptyList() throws Exception {
        // Given
        when(findUsuarioUseCase.findAll()).thenReturn(Set.of());

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findUsuarioUseCase).findAll();
    }

    @Test
    void testDeleteUsuario_Success() throws Exception {
        // Given
        doNothing().when(deleteUsuarioUseCase).execute(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Usuário removido com sucesso"));

        verify(deleteUsuarioUseCase).execute(1L);
    }

    @Test
    void testDeleteUsuario_NotFound() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Usuário não encontrado com id: 999"))
                .when(deleteUsuarioUseCase).execute(999L);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com id: 999"));

        verify(deleteUsuarioUseCase).execute(999L);
    }
} 