package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.*;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.usecase.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EntityScan(basePackages = "br.com.fiap.tech_challenge_2.infrastructure.persistence.entity")
public class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateRestauranteUseCase createRestauranteUseCase;

    @MockitoBean
    private FindRestauranteUseCase findRestauranteUseCase;

    @MockitoBean
    private UpdateRestauranteUseCase updateRestauranteUseCase;

    @MockitoBean
    private DeleteRestauranteUseCase deleteRestauranteUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private RestauranteRequest restauranteRequest;
    private RestauranteResponse restauranteResponse;
    private EnderecoDTO enderecoDTO;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private UsuarioResponse donoDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        donoDTO = new UsuarioResponse(
                1L,
                "João Silva",
                tipoUsuarioDTO,
                "joao@email.com",
                "joao123",
                enderecoDTO,
                LocalDate.now()
        );

        restauranteRequest = new RestauranteRequest(
                "Restaurante Teste",
                enderecoDTO,
                "Cozinha Teste",
                "12:00-16:00",
                1L
        );

        restauranteResponse = new RestauranteResponse(
                1L,
                "Restaurante Teste",
                enderecoDTO,
                "Cozinha Teste",
                "12:00-16:00",
                donoDTO,
                null
        );
    }

    @Test
    void testCreateRestaurante_Success() throws Exception {
        // Given
        when(createRestauranteUseCase.execute(any(RestauranteRequest.class))).thenReturn(restauranteResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"))
                .andExpect(jsonPath("$.data.horarioFuncionamento").value("12:00-16:00"))
                .andExpect(jsonPath("$.data.tipoCozinha").value("Cozinha Teste"))
                .andExpect(jsonPath("$.data.dono.id").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(createRestauranteUseCase).execute(any(RestauranteRequest.class));
    }

    @Test
    void testCreateRestaurante_DuplicateName() throws Exception {
        // Given
        when(createRestauranteUseCase.execute(any(RestauranteRequest.class)))
                .thenThrow(new DuplicateResourceException("Nome do restaurante já está em uso"));

        // When & Then
        mockMvc.perform(post("/api/v1/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Nome do restaurante já está em uso"));

        verify(createRestauranteUseCase).execute(any(RestauranteRequest.class));
    }

    @Test
    void testCreateRestaurante_InvalidRequest() throws Exception {
        // Given - Invalid request without required fields
        RestauranteRequest invalidRequest = new RestauranteRequest(
                "", // empty nome
                null, // null endereco
                "", // empty tipoCozinha
                "", // empty horarioFuncionamento
                null // null dono
        );

        // When & Then
        mockMvc.perform(post("/api/v1/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createRestauranteUseCase, never()).execute(any());
    }

    @Test
    void testUpdateRestaurante_Success() throws Exception {
        // Given
        when(updateRestauranteUseCase.execute(any(Long.class), any(RestauranteRequest.class))).thenReturn(restauranteResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/restaurantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"))
                .andExpect(jsonPath("$.data.horarioFuncionamento").value("12:00-16:00"))
                .andExpect(jsonPath("$.data.tipoCozinha").value("Cozinha Teste"))
                .andExpect(jsonPath("$.data.dono.id").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(updateRestauranteUseCase).execute(any(Long.class), any(RestauranteRequest.class));
    }

    @Test
    void testFindRestauranteById_Success() throws Exception {
        // Given
        when(findRestauranteUseCase.findById(1L)).thenReturn(restauranteResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/restaurantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nome").value("Restaurante Teste"))
                .andExpect(jsonPath("$.data.horarioFuncionamento").value("12:00-16:00"))
                .andExpect(jsonPath("$.data.tipoCozinha").value("Cozinha Teste"))
                .andExpect(jsonPath("$.data.dono.id").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findRestauranteUseCase).findById(1L);
    }

    @Test
    void testFindRestauranteById_NotFound() throws Exception {
        // Given
        when(findRestauranteUseCase.findById(999L))
                .thenThrow(new ResourceNotFoundException("Restaurante não encontrado com id: 999"));

        // When & Then
        mockMvc.perform(get("/api/v1/restaurantes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurante não encontrado com id: 999"));

        verify(findRestauranteUseCase).findById(999L);
    }

    @Test
    void testFindAllRestaurantes_Success() throws Exception {
        // Given
        Set<RestauranteResponse> restaurantes = Set.of(restauranteResponse);
        when(findRestauranteUseCase.findAll()).thenReturn(restaurantes);

        // When & Then
        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nome").value("Restaurante Teste"))
                .andExpect(jsonPath("$.data[0].horarioFuncionamento").value("12:00-16:00"))
                .andExpect(jsonPath("$.data[0].tipoCozinha").value("Cozinha Teste"))
                .andExpect(jsonPath("$.data[0].dono.id").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findRestauranteUseCase).findAll();
    }

    @Test
    void testFindAllRestaurantes_EmptyList() throws Exception {
        // Given
        when(findRestauranteUseCase.findAll()).thenReturn(Set.of());

        // When & Then
        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        verify(findRestauranteUseCase).findAll();
    }

    @Test
    void testDeleteRestaurante_Success() throws Exception {
        // Given
        doNothing().when(deleteRestauranteUseCase).execute(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/restaurantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Restaurante removido com sucesso"));

        verify(deleteRestauranteUseCase).execute(1L);
    }

    @Test
    void testDeleteUsuario_NotFound() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Restaurante não encontrado com id: 999"))
                .when(deleteRestauranteUseCase).execute(999L);

        // When & Then
        mockMvc.perform(delete("/api/v1/restaurantes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Restaurante não encontrado com id: 999"));

        verify(deleteRestauranteUseCase).execute(999L);
    }
}
