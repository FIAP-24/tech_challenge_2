package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.FindTipoUsuarioUseCaseImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private FindTipoUsuarioUseCaseImpl findTipoUsuarioUseCase;

    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;
    private List<TipoUsuario> tipoUsuarios;
    private List<TipoUsuarioDTO> tipoUsuariosDTO;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNome("PROPRIETARIO");
        
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        
        tipoUsuarios = List.of(tipoUsuario);
        tipoUsuariosDTO = List.of(tipoUsuarioDTO);
    }

    @Test
    void testFindAll_Success() {
        // Given
        when(repository.findAll()).thenReturn(tipoUsuarios);
        when(mapper.toDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        // When
        List<TipoUsuarioDTO> result = findTipoUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tipoUsuarioDTO, result.get(0));

        verify(repository).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<TipoUsuarioDTO> result = findTipoUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void testFindById_Success() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(mapper.toDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        // When
        TipoUsuarioDTO result = findTipoUsuarioUseCase.findById(1L);

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
            findTipoUsuarioUseCase.findById(999L);
        });

        assertEquals("Tipo de usuário não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testFindById_WithNullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findTipoUsuarioUseCase.findById(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());

        verify(repository, never()).findById(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testFindAll_WithRealTypes() {
        // Given
        TipoUsuario proprietario = new TipoUsuario();
        proprietario.setId(1L);
        proprietario.setNome("PROPRIETARIO");
        
        TipoUsuario cliente = new TipoUsuario();
        cliente.setId(2L);
        cliente.setNome("CLIENTE");
        
        List<TipoUsuario> tipos = List.of(proprietario, cliente);
        
        TipoUsuarioDTO proprietarioDTO = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        TipoUsuarioDTO clienteDTO = new TipoUsuarioDTO(2L, "CLIENTE");
        
        List<TipoUsuarioDTO> tiposDTO = List.of(proprietarioDTO, clienteDTO);

        when(repository.findAll()).thenReturn(tipos);
        when(mapper.toDTO(proprietario)).thenReturn(proprietarioDTO);
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        // When
        List<TipoUsuarioDTO> result = findTipoUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PROPRIETARIO", result.get(0).nome());
        assertEquals("CLIENTE", result.get(1).nome());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(repository).findAll();
    }

    @Test
    void testFindById_Proprietario() {
        // Given
        TipoUsuario proprietario = new TipoUsuario();
        proprietario.setId(1L);
        proprietario.setNome("PROPRIETARIO");
        
        TipoUsuarioDTO proprietarioDTO = new TipoUsuarioDTO(1L, "PROPRIETARIO");

        when(repository.findById(1L)).thenReturn(Optional.of(proprietario));
        when(mapper.toDTO(proprietario)).thenReturn(proprietarioDTO);

        // When
        TipoUsuarioDTO result = findTipoUsuarioUseCase.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("PROPRIETARIO", result.nome());
        assertEquals(1L, result.id());

        verify(repository).findById(1L);
        verify(mapper).toDTO(proprietario);
    }

    @Test
    void testFindById_Cliente() {
        // Given
        TipoUsuario cliente = new TipoUsuario();
        cliente.setId(2L);
        cliente.setNome("CLIENTE");
        
        TipoUsuarioDTO clienteDTO = new TipoUsuarioDTO(2L, "CLIENTE");

        when(repository.findById(2L)).thenReturn(Optional.of(cliente));
        when(mapper.toDTO(cliente)).thenReturn(clienteDTO);

        // When
        TipoUsuarioDTO result = findTipoUsuarioUseCase.findById(2L);

        // Then
        assertNotNull(result);
        assertEquals("CLIENTE", result.nome());
        assertEquals(2L, result.id());

        verify(repository).findById(2L);
        verify(mapper).toDTO(cliente);
    }
} 