package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
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
    private Usuario usuario;
    private UsuarioResponse usuarioResponse;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        usuarioRequest = new UsuarioRequest(
            "João Silva",
            "joao@email.com",
            Perfil.CLIENTE,
            "joao123",
            "123456",
            enderecoDTO
        );

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("hashedPassword");
        usuario.setDataUpdate(LocalDate.now());

        usuarioResponse = new UsuarioResponse(
            1L,
            "João Silva",
            Perfil.CLIENTE,
            "joao@email.com",
            "joao123",
            enderecoDTO,
            LocalDate.now()
        );
    }

    @Test
    void testSave_Success() {
        // Given
        when(usuarioDomainService.isLoginAvailable("joao123")).thenReturn(true);
        when(usuarioMapper.toEntity(usuarioRequest)).thenReturn(usuario);
        when(passwordHasher.hashPassword("123456")).thenReturn("hashedPassword");
        when(usuarioDomainService.createUser(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = usuarioService.save(usuarioRequest);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.nome(), result.nome());
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.login(), result.login());
        assertEquals(usuarioResponse.perfil(), result.perfil());

        verify(usuarioDomainService).isLoginAvailable("joao123");
        verify(usuarioMapper).toEntity(usuarioRequest);
        verify(passwordHasher).hashPassword("123456");
        verify(usuarioDomainService).createUser(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testSave_DuplicateLogin() {
        // Given
        when(usuarioDomainService.isLoginAvailable("joao123")).thenReturn(false);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            usuarioService.save(usuarioRequest);
        });

        verify(usuarioDomainService).isLoginAvailable("joao123");
        verify(usuarioMapper, never()).toEntity(any());
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService, never()).createUser(any());
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        Long id = 1L;
        when(usuarioDomainService.findUserById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = usuarioService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.nome(), result.nome());
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.login(), result.login());
        assertEquals(usuarioResponse.perfil(), result.perfil());

        verify(usuarioDomainService).findUserById(id);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        Long id = 999L;
        when(usuarioDomainService.findUserById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.findById(id);
        });

        verify(usuarioDomainService).findUserById(id);
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Usuario> usuarios = List.of(usuario);
        when(usuarioDomainService.findAllUsers()).thenReturn(usuarios);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        Set<UsuarioResponse> result = usuarioService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(u -> u.id().equals(usuarioResponse.id())));

        verify(usuarioDomainService).findAllUsers();
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(usuarioDomainService.findAllUsers()).thenReturn(List.of());

        // When
        Set<UsuarioResponse> result = usuarioService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(usuarioDomainService).findAllUsers();
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testDelete_Success() {
        // Given
        Long id = 1L;
        when(usuarioDomainService.findUserById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioDomainService).deleteUser(id);

        // When
        usuarioService.delete(id);

        // Then
        verify(usuarioDomainService).findUserById(id);
        verify(usuarioDomainService).deleteUser(id);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        Long id = 999L;
        when(usuarioDomainService.findUserById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.delete(id);
        });

        verify(usuarioDomainService).findUserById(id);
        verify(usuarioDomainService, never()).deleteUser(any());
    }
} 