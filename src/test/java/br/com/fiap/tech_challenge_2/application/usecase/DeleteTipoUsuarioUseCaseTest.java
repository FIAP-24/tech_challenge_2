package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.DeleteTipoUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository repository;

    @InjectMocks
    private DeleteTipoUsuarioUseCaseImpl deleteTipoUsuarioUseCase;

    @Test
    void testExecute_Success() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> deleteTipoUsuarioUseCase.execute(1L));

        // Then
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testExecute_WithNullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deleteTipoUsuarioUseCase.execute(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());

        verify(repository, never()).existsById(any());
        verify(repository, never()).deleteById(any());
    }

    @Test
    void testExecute_NotFound() {
        // Given
        when(repository.existsById(999L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteTipoUsuarioUseCase.execute(999L);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).existsById(999L);
        verify(repository, never()).deleteById(any());
    }
} 