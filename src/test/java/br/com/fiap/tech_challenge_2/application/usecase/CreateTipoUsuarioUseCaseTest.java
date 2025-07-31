package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateTipoUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private CreateTipoUsuarioUseCaseImpl createTipoUsuarioUseCase;

    private TipoUsuarioDTO validRequest;
    private TipoUsuarioDTO expectedResponse;
    private TipoUsuario domainTipoUsuario;
    private TipoUsuario savedTipoUsuario;

    @BeforeEach
    void setUp() {
        validRequest = new TipoUsuarioDTO(null, "PROPRIETARIO");
        expectedResponse = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        
        domainTipoUsuario = new TipoUsuario();
        domainTipoUsuario.setNome("PROPRIETARIO");
        
        savedTipoUsuario = new TipoUsuario();
        savedTipoUsuario.setId(1L);
        savedTipoUsuario.setNome("PROPRIETARIO");
    }

    @Test
    void testExecute_Success() {
        // Given
        when(mapper.toEntity(validRequest)).thenReturn(domainTipoUsuario);
        when(repository.save(domainTipoUsuario)).thenReturn(savedTipoUsuario);
        when(mapper.toDTO(savedTipoUsuario)).thenReturn(expectedResponse);

        // When
        TipoUsuarioDTO result = createTipoUsuarioUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());

        verify(mapper).toEntity(validRequest);
        verify(repository).save(domainTipoUsuario);
        verify(mapper).toDTO(savedTipoUsuario);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTipoUsuarioUseCase.execute(null);
        });

        assertEquals("Request cannot be null", exception.getMessage());

        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithEmptyNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(null, "");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTipoUsuarioUseCase.execute(invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithNullNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(null, null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTipoUsuarioUseCase.execute(invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_WithWhitespaceNome() {
        // Given
        TipoUsuarioDTO invalidRequest = new TipoUsuarioDTO(null, "   ");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTipoUsuarioUseCase.execute(invalidRequest);
        });

        assertEquals("Nome is required", exception.getMessage());

        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testExecute_CreateProprietario() {
        // Given
        TipoUsuarioDTO proprietarioRequest = new TipoUsuarioDTO(null, "PROPRIETARIO");
        TipoUsuarioDTO proprietarioResponse = new TipoUsuarioDTO(1L, "PROPRIETARIO");
        
        TipoUsuario domainProprietario = new TipoUsuario();
        domainProprietario.setNome("PROPRIETARIO");
        
        TipoUsuario savedProprietario = new TipoUsuario();
        savedProprietario.setId(1L);
        savedProprietario.setNome("PROPRIETARIO");

        when(mapper.toEntity(proprietarioRequest)).thenReturn(domainProprietario);
        when(repository.save(domainProprietario)).thenReturn(savedProprietario);
        when(mapper.toDTO(savedProprietario)).thenReturn(proprietarioResponse);

        // When
        TipoUsuarioDTO result = createTipoUsuarioUseCase.execute(proprietarioRequest);

        // Then
        assertNotNull(result);
        assertEquals("PROPRIETARIO", result.nome());
        assertEquals(1L, result.id());

        verify(mapper).toEntity(proprietarioRequest);
        verify(repository).save(domainProprietario);
        verify(mapper).toDTO(savedProprietario);
    }

    @Test
    void testExecute_CreateCliente() {
        // Given
        TipoUsuarioDTO clienteRequest = new TipoUsuarioDTO(null, "CLIENTE");
        TipoUsuarioDTO clienteResponse = new TipoUsuarioDTO(2L, "CLIENTE");
        
        TipoUsuario domainCliente = new TipoUsuario();
        domainCliente.setNome("CLIENTE");
        
        TipoUsuario savedCliente = new TipoUsuario();
        savedCliente.setId(2L);
        savedCliente.setNome("CLIENTE");

        when(mapper.toEntity(clienteRequest)).thenReturn(domainCliente);
        when(repository.save(domainCliente)).thenReturn(savedCliente);
        when(mapper.toDTO(savedCliente)).thenReturn(clienteResponse);

        // When
        TipoUsuarioDTO result = createTipoUsuarioUseCase.execute(clienteRequest);

        // Then
        assertNotNull(result);
        assertEquals("CLIENTE", result.nome());
        assertEquals(2L, result.id());

        verify(mapper).toEntity(clienteRequest);
        verify(repository).save(domainCliente);
        verify(mapper).toDTO(savedCliente);
    }
} 