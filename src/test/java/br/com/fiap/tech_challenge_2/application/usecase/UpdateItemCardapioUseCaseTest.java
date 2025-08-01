package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.ItemCardapioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.UpdateItemCardapioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.repository.ItemCardapioRepository;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
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
class UpdateItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    @InjectMocks
    private UpdateItemCardapioUseCaseImpl updateItemCardapioUseCase;

    private ItemCardapioRequestDTO validRequest;
    private ItemCardapioResponse expectedResponse;
    private ItemCardapio existingItem;
    private ItemCardapio updatedItem;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        validRequest = new ItemCardapioRequestDTO(
                "X-Burger Atualizado",
                "Hambúrguer delicioso com queijo",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                1L
        );

        expectedResponse = new ItemCardapioResponse(
                1L,
                "X-Burger Atualizado",
                "Hambúrguer delicioso com queijo",
                new BigDecimal("25.90"),
                false,
                "/fotos/x-burger.jpg",
                "R$ 25,90"
        );

        existingItem = new ItemCardapio();
        existingItem.setId(1L);
        existingItem.setNome("X-Burger Original");
        existingItem.setDescricao("Hambúrguer original");
        existingItem.setPreco(new BigDecimal("20.00"));
        existingItem.setDisponivelApenasNoLocal(true);
        existingItem.setFotoPath("/fotos/x-burger-original.jpg");
        existingItem.setRestaurante(restaurante);

        updatedItem = new ItemCardapio();
        updatedItem.setId(1L);
        updatedItem.setNome("X-Burger Atualizado");
        updatedItem.setDescricao("Hambúrguer delicioso com queijo");
        updatedItem.setPreco(new BigDecimal("25.90"));
        updatedItem.setDisponivelApenasNoLocal(false);
        updatedItem.setFotoPath("/fotos/x-burger.jpg");
        updatedItem.setRestaurante(restaurante);
    }

    @Test
    void testExecute_Success() {
        // Given
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(restauranteRepository.existsById(1L)).thenReturn(true);
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(updatedItem);
        when(itemCardapioMapper.toResponse(updatedItem)).thenReturn(expectedResponse);

        // When
        ItemCardapioResponse result = updateItemCardapioUseCase.execute(1L, validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());
        assertEquals(expectedResponse.descricao(), result.descricao());
        assertEquals(expectedResponse.preco(), result.preco());
        assertEquals(expectedResponse.disponivelApenasNoLocal(), result.disponivelApenasNoLocal());
        assertEquals(expectedResponse.fotoPath(), result.fotoPath());

        verify(itemCardapioRepository).findById(1L);
        verify(restauranteRepository).existsById(1L);
        verify(itemCardapioRepository).save(any(ItemCardapio.class));
        verify(itemCardapioMapper).toResponse(updatedItem);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateItemCardapioUseCase.execute(1L, null);
        });

        assertEquals("Request cannot be null", exception.getMessage());

        verify(itemCardapioRepository, never()).findById(any());
        verify(restauranteRepository, never()).existsById(any());
        verify(itemCardapioRepository, never()).save(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_ItemNotFound() {
        // Given
        when(restauranteRepository.existsById(1L)).thenReturn(true);
        when(itemCardapioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            updateItemCardapioUseCase.execute(999L, validRequest);
        });

        assertEquals("Item do cardápio não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).existsById(1L);
        verify(itemCardapioRepository).findById(999L);
        verify(itemCardapioRepository, never()).save(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_RestauranteNotFound() {
        // Given
        when(restauranteRepository.existsById(999L)).thenReturn(false);

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
            updateItemCardapioUseCase.execute(1L, requestWithInvalidRestaurante);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).existsById(999L);
        verify(itemCardapioRepository, never()).findById(any());
        verify(itemCardapioRepository, never()).save(any());
        verify(itemCardapioMapper, never()).toResponse(any());
    }
} 