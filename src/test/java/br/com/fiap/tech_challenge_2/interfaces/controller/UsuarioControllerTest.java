package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.*;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.usecase.*;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @MockitoBean
    private CreateUsuarioUseCase createUsuarioUseCase;

    @MockitoBean
    private FindUsuarioUseCase findUsuarioUseCase;

    @MockitoBean
    private UpdateUsuarioUseCase updateUsuarioUseCase;

    @MockitoBean
    private DeleteUsuarioUseCase deleteUsuarioUseCase;

    @MockitoBean
    private AuthenticateUsuarioUseCase authenticateUsuarioUseCase;

    @MockitoBean
    private UpdatePassUsuarioUseCase updatePassUsuarioUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;
    private EnderecoDTO enderecoDTO;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private UsuarioEditRequest usuarioEditRequest;
    private UsuarioEditPassRequest usuarioEditPassRequest;
    private UsuarioLoginRequest usuarioLoginRequest;

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

        usuarioEditRequest = new UsuarioEditRequest(
                "João Silva",
                "joao@email.com",
                1l,
                "123456",
                enderecoDTO
        );

        usuarioEditPassRequest = new UsuarioEditPassRequest(
                "joao123",
                "123456",
                "6543213",
                "6543213"
        );

        usuarioLoginRequest = new UsuarioLoginRequest(
                "joao123",
                "123456"
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
    void testUpdateUsuario_Success() throws Exception {
        // Given
        when(updateUsuarioUseCase.execute(any(Long.class), any(UsuarioEditRequest.class))).thenReturn(usuarioResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEditRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("João Silva"))
                .andExpect(jsonPath("$.data.email").value("joao@email.com"))
                .andExpect(jsonPath("$.data.login").value("joao123"))
                .andExpect(jsonPath("$.data.tipoUsuario.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(updateUsuarioUseCase).execute(any(Long.class), any(UsuarioEditRequest.class));
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

    @Test
    void testUpdatePass_Success() throws Exception {
        // Given
        when(updatePassUsuarioUseCase.execute(any(UsuarioEditPassRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/atualizar-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEditPassRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(updatePassUsuarioUseCase).execute(any(UsuarioEditPassRequest.class));
    }

    @Test
    void testUsuarioLogin_Success() throws Exception {
        // Given
        when(authenticateUsuarioUseCase.execute(any(UsuarioLoginRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(authenticateUsuarioUseCase).execute(any(UsuarioLoginRequest.class));
    }

    @Test
    void testUsuarioLogin_Wrong() throws Exception {
        // Given
        doThrow(new AuthenticationException("Senha incorreta"))
                .when(authenticateUsuarioUseCase).execute(any(UsuarioLoginRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioLoginRequest)))
                .andExpect(status().isUnauthorized());


        verify(authenticateUsuarioUseCase).execute(any(UsuarioLoginRequest.class));
    }
} 