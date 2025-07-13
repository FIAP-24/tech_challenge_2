package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceImplTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private TipoUsuarioServiceImpl tipoUsuarioService;

    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "Cliente");
        
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNome("Cliente");
    }

    @Test
    void testCreate_Success() {
        // Given
        when(mapper.toEntity(tipoUsuarioDTO)).thenReturn(tipoUsuario);
        when(repository.save(tipoUsuario)).thenReturn(tipoUsuario);
        when(mapper.toDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        // When
        TipoUsuarioDTO result = tipoUsuarioService.create(tipoUsuarioDTO);

        // Then
        assertNotNull(result);
        assertEquals(tipoUsuarioDTO, result);

        verify(mapper).toEntity(tipoUsuarioDTO);
        verify(repository).save(tipoUsuario);
        verify(mapper).toDTO(tipoUsuario);
    }

    @Test
    void testFindById_Success() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(mapper.toDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        // When
        TipoUsuarioDTO result = tipoUsuarioService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(tipoUsuarioDTO, result);

        verify(repository).findById(1L);
        verify(mapper).toDTO(tipoUsuario);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tipoUsuarioService.findById(999L);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<TipoUsuario> tipos = List.of(tipoUsuario);
        when(repository.findAll()).thenReturn(tipos);
        when(mapper.toDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        // When
        List<TipoUsuarioDTO> result = tipoUsuarioService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tipoUsuarioDTO, result.get(0));

        verify(repository).findAll();
        verify(mapper).toDTO(tipoUsuario);
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<TipoUsuarioDTO> result = tipoUsuarioService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testUpdate_Success() {
        // Given
        TipoUsuarioDTO updateDTO = new TipoUsuarioDTO(1L, "Administrador");
        when(repository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(repository.save(tipoUsuario)).thenReturn(tipoUsuario);
        when(mapper.toDTO(tipoUsuario)).thenReturn(updateDTO);

        // When
        TipoUsuarioDTO result = tipoUsuarioService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Administrador", tipoUsuario.getNome());

        verify(repository).findById(1L);
        verify(repository).save(tipoUsuario);
        verify(mapper).toDTO(tipoUsuario);
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tipoUsuarioService.update(999L, tipoUsuarioDTO);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testDelete_Success() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> tipoUsuarioService.delete(1L));

        // Then
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(repository.existsById(999L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            tipoUsuarioService.delete(999L);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).existsById(999L);
        verify(repository, never()).deleteById(any());
    }
} 