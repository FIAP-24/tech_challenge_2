package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.FindItemCardapioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioDomainService itemCardapioDomainService;

    @InjectMocks
    private FindItemCardapioUseCaseImpl findItemCardapioUseCase;

    private ItemCardapio itemCardapio;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        
        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Pizza Margherita");
        itemCardapio.setDescricao("Pizza tradicional italiana");
        itemCardapio.setPreco(25.90);
        itemCardapio.setDisponivelApenasNoLocal(true);
        itemCardapio.setFotoPath("/fotos/pizza.jpg");
        itemCardapio.setRestaurante(restaurante);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<ItemCardapio> itens = List.of(itemCardapio);
        when(itemCardapioDomainService.findAllItemCardapios()).thenReturn(itens);

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemCardapio, result.get(0));
        
        verify(itemCardapioDomainService).findAllItemCardapios();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(itemCardapioDomainService.findAllItemCardapios()).thenReturn(List.of());

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(itemCardapioDomainService).findAllItemCardapios();
    }

    @Test
    void testFindById_Success() {
        // Given
        when(itemCardapioDomainService.findItemCardapioById(1L)).thenReturn(Optional.of(itemCardapio));

        // When
        ItemCardapio result = findItemCardapioUseCase.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(itemCardapio, result);
        
        verify(itemCardapioDomainService).findItemCardapioById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(itemCardapioDomainService.findItemCardapioById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            findItemCardapioUseCase.findById(999L);
        });
        
        assertEquals("Item do cardápio não encontrado com id: 999", exception.getMessage());
        
        verify(itemCardapioDomainService).findItemCardapioById(999L);
    }

    @Test
    void testFindById_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findItemCardapioUseCase.findById(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        
        verify(itemCardapioDomainService, never()).findItemCardapioById(any());
    }

    @Test
    void testFindByRestauranteId_Success() {
        // Given
        List<ItemCardapio> itens = List.of(itemCardapio);
        when(itemCardapioDomainService.findItemCardapiosByRestauranteId(1L)).thenReturn(itens);

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findByRestauranteId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemCardapio, result.get(0));
        
        verify(itemCardapioDomainService).findItemCardapiosByRestauranteId(1L);
    }

    @Test
    void testFindByRestauranteId_EmptyList() {
        // Given
        when(itemCardapioDomainService.findItemCardapiosByRestauranteId(1L)).thenReturn(List.of());

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findByRestauranteId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(itemCardapioDomainService).findItemCardapiosByRestauranteId(1L);
    }

    @Test
    void testFindByRestauranteId_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findItemCardapioUseCase.findByRestauranteId(null);
        });
        
        assertEquals("Restaurante ID cannot be null", exception.getMessage());
        
        verify(itemCardapioDomainService, never()).findItemCardapiosByRestauranteId(any());
    }

    @Test
    void testFindAvailableByRestauranteId_Success() {
        // Given
        List<ItemCardapio> itens = List.of(itemCardapio);
        when(itemCardapioDomainService.findAvailableItemCardapiosByRestauranteId(1L)).thenReturn(itens);

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findAvailableByRestauranteId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemCardapio, result.get(0));
        
        verify(itemCardapioDomainService).findAvailableItemCardapiosByRestauranteId(1L);
    }

    @Test
    void testFindAvailableByRestauranteId_EmptyList() {
        // Given
        when(itemCardapioDomainService.findAvailableItemCardapiosByRestauranteId(1L)).thenReturn(List.of());

        // When
        List<ItemCardapio> result = findItemCardapioUseCase.findAvailableByRestauranteId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(itemCardapioDomainService).findAvailableItemCardapiosByRestauranteId(1L);
    }

    @Test
    void testFindAvailableByRestauranteId_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findItemCardapioUseCase.findAvailableByRestauranteId(null);
        });
        
        assertEquals("Restaurante ID cannot be null", exception.getMessage());
        
        verify(itemCardapioDomainService, never()).findAvailableItemCardapiosByRestauranteId(any());
    }
} 