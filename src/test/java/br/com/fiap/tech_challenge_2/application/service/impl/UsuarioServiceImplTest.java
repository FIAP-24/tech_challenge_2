package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequest usuarioRequest;
    private Usuario domainUsuario;
    private UsuarioResponse usuarioResponse;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        
        usuarioRequest = new UsuarioRequest("John Doe", "john@email.com", 1l, "johndoe", "password123", null);
        
        domainUsuario = new Usuario();
        domainUsuario.setId(1L);
        domainUsuario.setNome("John Doe");
        domainUsuario.setEmail("john@email.com");
        domainUsuario.setLogin("johndoe");
        domainUsuario.setTipoUsuario(tipoUsuario);
        domainUsuario.setDataUpdate(LocalDate.now());

        usuarioResponse = new UsuarioResponse(1L, "John Doe", tipoUsuarioDTO, "john@email.com", "johndoe", null, LocalDate.now());
    }

    @Test
    void testSave_Success() {
        // Given
        when(usuarioDomainService.isLoginAvailable(any())).thenReturn(true);
        when(passwordHasher.hashPassword(any())).thenReturn("hashedPassword");
        when(usuarioMapper.toEntity(any(UsuarioRequest.class))).thenReturn(domainUsuario);
        when(usuarioDomainService.createUser(any(Usuario.class))).thenReturn(domainUsuario);
        when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = usuarioService.save(usuarioRequest);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.nome(), result.nome());
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.login(), result.login());
        assertEquals(usuarioResponse.tipoUsuario().nome(), result.tipoUsuario().nome());

        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(passwordHasher).hashPassword("password123");
        verify(usuarioMapper).toEntity(usuarioRequest);
        verify(usuarioDomainService).createUser(domainUsuario);
        verify(usuarioMapper).toResponse(domainUsuario);
    }

    @Test
    void testSave_WithNullRequest() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            usuarioService.save(null);
        });

        verify(usuarioDomainService, never()).createUser(any());
    }

    @Test
    void testSave_WithDuplicateLogin() {
        // Given
        when(usuarioDomainService.isLoginAvailable(any())).thenReturn(false);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            usuarioService.save(usuarioRequest);
        });

        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(usuarioDomainService, never()).createUser(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainUsuario));
        when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = usuarioService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.nome(), result.nome());
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.login(), result.login());
        assertEquals(usuarioResponse.tipoUsuario().nome(), result.tipoUsuario().nome());

        verify(usuarioDomainService).findUserById(1L);
        verify(usuarioMapper).toResponse(domainUsuario);
    }

    @Test
    void testFindById_WithNullId() {
        // Given
        when(usuarioDomainService.findUserById(null)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.findById(null);
        });

        verify(usuarioDomainService).findUserById(null);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(usuarioDomainService.findUserById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.findById(999L);
        });

        verify(usuarioDomainService).findUserById(999L);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Usuario> domainUsuarios = List.of(domainUsuario);
        Set<UsuarioResponse> expectedResponses = Set.of(usuarioResponse);
        when(usuarioDomainService.findAllUsers()).thenReturn(domainUsuarios);

        // When
        Set<UsuarioResponse> result = usuarioService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(usuarioDomainService).findAllUsers();
        verify(usuarioMapper).toResponse(domainUsuario);
    }
} 