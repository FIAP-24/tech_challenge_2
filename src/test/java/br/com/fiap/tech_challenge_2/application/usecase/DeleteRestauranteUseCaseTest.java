package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.DeleteRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteRestauranteUseCaseTest {

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @InjectMocks
    private DeleteRestauranteUseCaseImpl deleteRestauranteUseCase;

    private Restaurante restaurante;
    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("12:00-22:00");
    }

    @Test
    void testExecute_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(restaurante));
        doNothing().when(restauranteDomainService).deleteRestaurante(1L);

        // When
        assertDoesNotThrow(() -> deleteRestauranteUseCase.execute(1L));

        // Then
        verify(restauranteDomainService).findRestauranteById(1L);
        verify(restauranteDomainService).deleteRestaurante(1L);
    }

    @Test
    void testExecute_RestauranteNotFound() {
        // Given
        when(restauranteDomainService.findRestauranteById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteRestauranteUseCase.execute(999L);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteDomainService).findRestauranteById(999L);
        verify(restauranteDomainService, never()).deleteRestaurante(any());
    }

    @Test
    void testExecute_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deleteRestauranteUseCase.execute(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());

        verify(restauranteDomainService, never()).findRestauranteById(any());
        verify(restauranteDomainService, never()).deleteRestaurante(any());
    }

    @Test
    void testExecute_DomainServiceThrowsException() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(restaurante));
        doThrow(new RuntimeException("Database error")).when(restauranteDomainService).deleteRestaurante(1L);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteRestauranteUseCase.execute(1L);
        });

        assertEquals("Database error", exception.getMessage());

        verify(restauranteDomainService).findRestauranteById(1L);
        verify(restauranteDomainService).deleteRestaurante(1L);
    }
}
