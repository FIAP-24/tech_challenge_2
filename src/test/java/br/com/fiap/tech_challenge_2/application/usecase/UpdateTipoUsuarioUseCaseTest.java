package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.UpdateTipoUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private UpdateTipoUsuarioUseCaseImpl updateTipoUsuarioUseCase;

    private TipoUsuarioDTO validRequest;
    private TipoUsuarioDTO expectedResponse;
    private TipoUsuario existingTipoUsuario;
    private TipoUsuario updatedTipoUsuario;

    @BeforeEach
    void setUp() {
        validRequest = new TipoUsuarioDTO(1L, "CLIENTE");
        expectedResponse = new TipoUsuarioDTO(1L, "CLIENTE");
        
        existingTipoUsuario = new TipoUsuario();
        existingTipoUsuario.setId(1L);
        existingTipoUsuario.setNome("PROPRIETARIO");
        
        updatedTipoUsuario = new TipoUsuario();
        updatedTipoUsuario.setId(1L);
        updatedTipoUsuario.setNome("CLIENTE");
    }

    @Test
    void testExecute_Success() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(existingTipoUsuario));
        when(repository.save(any(TipoUsuario.class))).thenReturn(updatedTipoUsuario);
        when(mapper.toDTO(updatedTipoUsuario)).thenReturn(expectedResponse);

        // When
        TipoUsuarioDTO result = updateTipoUsuarioUseCase.execute(1L, validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());

        verify(repository).findById(1L);
        verify(repository).save(any(TipoUsuario.class));
        verify(mapper).toDTO(updatedTipoUsuario);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateTipoUsuarioUseCase.execute(1L, null);
        });

        assertEquals("Request cannot be null", exception.getMessage());

        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithEmptyNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(1L, "");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateTipoUsuarioUseCase.execute(1L, invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithNullNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(1L, null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateTipoUsuarioUseCase.execute(1L, invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithWhitespaceNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(1L, "   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateTipoUsuarioUseCase.execute(1L, invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_NotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            updateTipoUsuarioUseCase.execute(999L, validRequest);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }
} 