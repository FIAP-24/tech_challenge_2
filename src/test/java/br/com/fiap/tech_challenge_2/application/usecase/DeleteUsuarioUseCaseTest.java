package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.DeleteUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @InjectMocks
    private DeleteUsuarioUseCaseImpl deleteUsuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setDataUpdate(LocalDate.now());
    }

    @Test
    void testExecute_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioDomainService).deleteUser(1L);

        // When
        assertDoesNotThrow(() -> deleteUsuarioUseCase.execute(1L));

        // Then
        verify(usuarioDomainService).findUserById(1L);
        verify(usuarioDomainService).deleteUser(1L);
    }

    @Test
    void testExecute_UserNotFound() {
        // Given
        when(usuarioDomainService.findUserById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteUsuarioUseCase.execute(999L);
        });
        
        assertEquals("Usuário não encontrado com id: 999", exception.getMessage());
        
        verify(usuarioDomainService).findUserById(999L);
        verify(usuarioDomainService, never()).deleteUser(any());
    }

    @Test
    void testExecute_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deleteUsuarioUseCase.execute(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserById(any());
        verify(usuarioDomainService, never()).deleteUser(any());
    }

    @Test
    void testExecute_DomainServiceThrowsException() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        doThrow(new RuntimeException("Database error")).when(usuarioDomainService).deleteUser(1L);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteUsuarioUseCase.execute(1L);
        });
        
        assertEquals("Database error", exception.getMessage());
        
        verify(usuarioDomainService).findUserById(1L);
        verify(usuarioDomainService).deleteUser(1L);
    }
} 