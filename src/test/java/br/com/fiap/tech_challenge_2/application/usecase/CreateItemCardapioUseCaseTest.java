package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.ItemCardapioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateItemCardapioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioDomainService itemCardapioDomainService;

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    @InjectMocks
    private CreateItemCardapioUseCaseImpl createItemCardapioUseCase;

    private ItemCardapioRequestDTO validRequest;
    private ItemCardapioResponse expectedResponse;
    private ItemCardapio domainItem;
    private ItemCardapio savedItem;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        validRequest = new ItemCardapioRequestDTO(
                "X-Burger",
                "Hambúrguer delicioso com queijo",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        expectedResponse = new ItemCardapioResponse(
                1L,
                "X-Burger",
                "Hambúrguer delicioso com queijo",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                "R$ 25,90"
        );

        domainItem = new ItemCardapio();
        domainItem.setNome("X-Burger");
        domainItem.setDescricao("Hambúrguer delicioso com queijo");
        domainItem.setPreco(new BigDecimal("25.90"));
        domainItem.setDisponivelApenasNoLocal(false);
        domainItem.setFotoPath("/fotos/x-burger.jpg");
        domainItem.setRestaurante(restaurante);

        savedItem = new ItemCardapio();
        savedItem.setId(1L);
        savedItem.setNome("X-Burger");
        savedItem.setDescricao("Hambúrguer delicioso com queijo");
        savedItem.setPreco(new BigDecimal("25.90"));
        savedItem.setDisponivelApenasNoLocal(false);
        savedItem.setFotoPath("/fotos/x-burger.jpg");
        savedItem.setRestaurante(restaurante);
    }

    @Test
    void testExecute_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(restaurante));
        when(itemCardapioMapper.toEntity(validRequest)).thenReturn(domainItem);
        when(itemCardapioDomainService.createItemCardapio(domainItem)).thenReturn(savedItem);
        when(itemCardapioMapper.toResponse(savedItem)).thenReturn(expectedResponse);

        // When
        ItemCardapioResponse result = createItemCardapioUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());
        assertEquals(expectedResponse.descricao(), result.descricao());
        assertEquals(expectedResponse.preco(), result.preco());
        assertEquals(expectedResponse.disponivelApenasNoLocal(), result.disponivelApenasNoLocal());
        assertEquals(expectedResponse.fotoPath(), result.fotoPath());

        verify(restauranteDomainService).findRestauranteById(1L);
        verify(itemCardapioMapper).toEntity(validRequest);
        verify(itemCardapioDomainService).createItemCardapio(domainItem);
        verify(itemCardapioMapper).toResponse(savedItem);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(null);
        });

        assertEquals("Request cannot be null", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_WithEmptyNome() {
        // Given
        ItemCardapioRequestDTO invalidRequest = new ItemCardapioRequestDTO(
                "",
                "Descrição válida",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(invalidRequest);
        });

        assertEquals("Nome do item é obrigatório", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_WithNullNome() {
        // Given
        ItemCardapioRequestDTO invalidRequest = new ItemCardapioRequestDTO(
                null,
                "Descrição válida",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(invalidRequest);
        });

        assertEquals("Nome do item é obrigatório", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_WithNullPreco() {
        // Given
        ItemCardapioRequestDTO invalidRequest = new ItemCardapioRequestDTO(
                "Nome válido",
                "Descrição válida",
                null,
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(invalidRequest);
        });

        assertEquals("Preço deve ser maior que zero", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_WithZeroPreco() {
        // Given
        ItemCardapioRequestDTO invalidRequest = new ItemCardapioRequestDTO(
                "Nome válido",
                "Descrição válida",
                BigDecimal.ZERO,
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(invalidRequest);
        });

        assertEquals("Preço deve ser maior que zero", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_WithNullRestauranteId() {
        // Given
        ItemCardapioRequestDTO invalidRequest = new ItemCardapioRequestDTO(
                "Nome válido",
                "Descrição válida",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                null
        );

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createItemCardapioUseCase.execute(invalidRequest);
        });

        assertEquals("ID do restaurante é obrigatório", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_RestauranteNotFound() {
        // Given
        when(restauranteDomainService.findRestauranteById(999L)).thenReturn(Optional.empty());

        ItemCardapioRequestDTO requestWithInvalidRestaurante = new ItemCardapioRequestDTO(
                "Nome válido",
                "Descrição válida",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                999L
        );

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            createItemCardapioUseCase.execute(requestWithInvalidRestaurante);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteDomainService).findRestauranteById(999L);
        verify(itemCardapioMapper, never()).toEntity(any());
        verify(itemCardapioDomainService, never()).createItemCardapio(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }
} 