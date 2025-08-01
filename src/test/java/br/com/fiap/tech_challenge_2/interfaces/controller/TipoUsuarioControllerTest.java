package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.usecase.*;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@EntityScan(basePackages = "br.com.fiap.tech_challenge_2.infrastructure.persistence.entity")
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateTipoUsuarioUseCase createTipoUsuarioUseCase;

    @MockitoBean
    private FindTipoUsuarioUseCase findTipoUsuarioUseCase;

    @MockitoBean
    private UpdateTipoUsuarioUseCase updateTipoUsuarioUseCase;

    @MockitoBean
    private DeleteTipoUsuarioUseCase deleteTipoUsuarioUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuarioDTO createRequest;
    private TipoUsuarioDTO updateRequest;

    @BeforeEach
    void setUp() {
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        createRequest = new TipoUsuarioDTO(null, "PROPRIETARIO");
        updateRequest = new TipoUsuarioDTO(1L, "CLIENTE");
    }

    @Test
    void testCreateTipoUsuario_Success() throws Exception {
        // Given
        when(createTipoUsuarioUseCase.execute(any(TipoUsuarioDTO.class))).thenReturn(tipoUsuarioDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("PROPRIETARIO"))
                .andExpect(jsonPath("$.message").value("Tipo de usuário criado com sucesso"));

        verify(createTipoUsuarioUseCase).execute(any(TipoUsuarioDTO.class));
    }

    @Test
    void testCreateTipoUsuario_InvalidRequest() throws Exception {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(null, "");

        // When & Then
        mockMvc.perform(post("/api/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createTipoUsuarioUseCase, never()).execute(any());
    }

    @Test
    void testFindTipoUsuarioById_Success() throws Exception {
        // Given
        when(findTipoUsuarioUseCase.findById(1L)).thenReturn(tipoUsuarioDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("PROPRIETARIO"));

        verify(findTipoUsuarioUseCase).findById(1L);
    }

    @Test
    void testFindTipoUsuarioById_NotFound() throws Exception {
        // Given
        when(findTipoUsuarioUseCase.findById(999L))
                .thenThrow(new ResourceNotFoundException("Tipo de usuário não encontrado com id: 999"));

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario/999"))
                .andExpect(status().isNotFound());

        verify(findTipoUsuarioUseCase).findById(999L);
    }

    @Test
    void testFindAllTipoUsuarios_Success() throws Exception {
        // Given
        List<TipoUsuarioDTO> tipos = List.of(tipoUsuarioDTO);
        when(findTipoUsuarioUseCase.findAll()).thenReturn(tipos);

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("PROPRIETARIO"));

        verify(findTipoUsuarioUseCase).findAll();
    }

    @Test
    void testFindAllTipoUsuarios_EmptyList() throws Exception {
        // Given
        when(findTipoUsuarioUseCase.findAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(findTipoUsuarioUseCase).findAll();
    }

    @Test
    void testUpdateTipoUsuario_Success() throws Exception {
        // Given
        TipoUsuarioDTO updatedResponse = new TipoUsuarioDTO(1L, "CLIENTE");
        when(updateTipoUsuarioUseCase.execute(eq(1L), any(TipoUsuarioDTO.class))).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/tipos-usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.message").value("Tipo de usuário atualizado com sucesso"));

        verify(updateTipoUsuarioUseCase).execute(eq(1L), any(TipoUsuarioDTO.class));
    }

    @Test
    void testUpdateTipoUsuario_NotFound() throws Exception {
        // Given
        when(updateTipoUsuarioUseCase.execute(eq(999L), any(TipoUsuarioDTO.class)))
                .thenThrow(new ResourceNotFoundException("Tipo de usuário não encontrado com id: 999"));

        // When & Then
        mockMvc.perform(put("/api/v1/tipos-usuario/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(updateTipoUsuarioUseCase).execute(eq(999L), any(TipoUsuarioDTO.class));
    }

    @Test
    void testUpdateTipoUsuario_InvalidRequest() throws Exception {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(1L, "");

        // When & Then
        mockMvc.perform(put("/api/v1/tipos-usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(updateTipoUsuarioUseCase, never()).execute(any(), any());
    }

    @Test
    void testDeleteTipoUsuario_Success() throws Exception {
        // Given
        doNothing().when(deleteTipoUsuarioUseCase).execute(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Tipo de usuário removido com sucesso"));

        verify(deleteTipoUsuarioUseCase).execute(1L);
    }

    @Test
    void testDeleteTipoUsuario_NotFound() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Tipo de usuário não encontrado com id: 999"))
                .when(deleteTipoUsuarioUseCase).execute(999L);

        // When & Then
        mockMvc.perform(delete("/api/v1/tipos-usuario/999"))
                .andExpect(status().isNotFound());

        verify(deleteTipoUsuarioUseCase).execute(999L);
    }

    @Test
    void testCreateTipoUsuario_Proprietario() throws Exception {
        // Given
        TipoUsuarioDTO proprietarioRequest = new TipoUsuarioDTO(null, "PROPRIETARIO");
        TipoUsuarioDTO proprietarioResponse = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        
        when(createTipoUsuarioUseCase.execute(any(TipoUsuarioDTO.class))).thenReturn(proprietarioResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("PROPRIETARIO"))
                .andExpect(jsonPath("$.message").value("Tipo de usuário criado com sucesso"));

        verify(createTipoUsuarioUseCase).execute(any(TipoUsuarioDTO.class));
    }

    @Test
    void testCreateTipoUsuario_Cliente() throws Exception {
        // Given
        TipoUsuarioDTO clienteRequest = new TipoUsuarioDTO(null, "CLIENTE");
        TipoUsuarioDTO clienteResponse = new TipoUsuarioDTO(2L, "CLIENTE");
        
        when(createTipoUsuarioUseCase.execute(any(TipoUsuarioDTO.class))).thenReturn(clienteResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.nome").value("CLIENTE"))
                .andExpect(jsonPath("$.message").value("Tipo de usuário criado com sucesso"));

        verify(createTipoUsuarioUseCase).execute(any(TipoUsuarioDTO.class));
    }

    @Test
    void testFindAllTipoUsuarios_WithRealTypes() throws Exception {
        // Given
        TipoUsuarioDTO proprietario = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        TipoUsuarioDTO cliente = new TipoUsuarioDTO(2L, "CLIENTE");
        List<TipoUsuarioDTO> tipos = List.of(proprietario, cliente);
        
        when(findTipoUsuarioUseCase.findAll()).thenReturn(tipos);

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").value(hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("PROPRIETARIO"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].nome").value("CLIENTE"));

        verify(findTipoUsuarioUseCase).findAll();
    }

    @Test
    void testFindTipoUsuarioById_Proprietario() throws Exception {
        // Given
        TipoUsuarioDTO proprietario = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        when(findTipoUsuarioUseCase.findById(1L)).thenReturn(proprietario);

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("PROPRIETARIO"));

        verify(findTipoUsuarioUseCase).findById(1L);
    }

    @Test
    void testFindTipoUsuarioById_Cliente() throws Exception {
        // Given
        TipoUsuarioDTO cliente = new TipoUsuarioDTO(2L, "CLIENTE");
        when(findTipoUsuarioUseCase.findById(2L)).thenReturn(cliente);

        // When & Then
        mockMvc.perform(get("/api/v1/tipos-usuario/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.nome").value("CLIENTE"));

        verify(findTipoUsuarioUseCase).findById(2L);
    }
} 