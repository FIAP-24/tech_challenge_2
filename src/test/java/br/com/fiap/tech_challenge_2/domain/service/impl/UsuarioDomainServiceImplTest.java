package br.com.fiap.tech_challenge_2.domain.service.impl;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDomainServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDomainServiceImpl usuarioDomainService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("hashedPassword");
        usuario.setDataUpdate(LocalDate.now());
    }

    @Test
    void testCreateUser_Success() {
        // Given
        when(usuarioRepository.findByLogin("joao123")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario result = usuarioDomainService.createUser(usuario);

        // Then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getLogin(), result.getLogin());
        assertEquals(usuario.getSenha(), result.getSenha());

        verify(usuarioRepository).findByLogin("joao123");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCreateUser_InvalidUsuario() {
        // Given
        Usuario invalidUsuario = new Usuario();
        invalidUsuario.setNome(""); // Invalid: empty name
        invalidUsuario.setLogin(""); // Invalid: empty login
        invalidUsuario.setSenha(""); // Invalid: empty password

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioDomainService.createUser(invalidUsuario);
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testCreateUser_NullUsuario() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            usuarioDomainService.createUser(null);
        });

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testCreateUser_DuplicateLogin() {
        // Given
        when(usuarioRepository.findByLogin("joao123")).thenReturn(Optional.of(usuario));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioDomainService.createUser(usuario);
        });

        verify(usuarioRepository).findByLogin("joao123");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testFindUserById_Success() {
        // Given
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> result = usuarioDomainService.findUserById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(usuario.getId(), result.get().getId());
        assertEquals(usuario.getNome(), result.get().getNome());
        assertEquals(usuario.getEmail(), result.get().getEmail());
        assertEquals(usuario.getLogin(), result.get().getLogin());

        verify(usuarioRepository).findById(id);
    }

    @Test
    void testFindUserById_NotFound() {
        // Given
        Long id = 999L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> result = usuarioDomainService.findUserById(id);

        // Then
        assertFalse(result.isPresent());

        verify(usuarioRepository).findById(id);
    }

    @Test
    void testFindUserByLogin_Success() {
        // Given
        String login = "joao123";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.of(usuario));

        // When
        Optional<Usuario> result = usuarioDomainService.findUserByLogin(login);

        // Then
        assertTrue(result.isPresent());
        assertEquals(usuario.getId(), result.get().getId());
        assertEquals(usuario.getNome(), result.get().getNome());
        assertEquals(usuario.getEmail(), result.get().getEmail());
        assertEquals(usuario.getLogin(), result.get().getLogin());

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testFindUserByLogin_NotFound() {
        // Given
        String login = "nonexistent";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> result = usuarioDomainService.findUserByLogin(login);

        // Then
        assertFalse(result.isPresent());

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testFindAllUsers_Success() {
        // Given
        List<Usuario> usuarios = List.of(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // When
        List<Usuario> result = usuarioDomainService.findAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(usuario.getId(), result.get(0).getId());
        assertEquals(usuario.getNome(), result.get(0).getNome());
        assertEquals(usuario.getEmail(), result.get(0).getEmail());
        assertEquals(usuario.getLogin(), result.get(0).getLogin());

        verify(usuarioRepository).findAll();
    }

    @Test
    void testFindAllUsers_EmptyList() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(List.of());

        // When
        List<Usuario> result = usuarioDomainService.findAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(usuarioRepository).findAll();
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario result = usuarioDomainService.updateUser(usuario);

        // Then
        assertNotNull(result);
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getLogin(), result.getLogin());

        verify(usuarioRepository).existsById(usuario.getId());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Given
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioDomainService.updateUser(usuario);
        });

        verify(usuarioRepository).existsById(usuario.getId());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(id);

        // When
        usuarioDomainService.deleteUser(id);

        // Then
        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository).deleteById(id);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Given
        Long id = 999L;
        when(usuarioRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            usuarioDomainService.deleteUser(id);
        });

        verify(usuarioRepository).existsById(id);
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void testIsLoginAvailable_Available() {
        // Given
        String login = "newlogin";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.empty());

        // When
        boolean result = usuarioDomainService.isLoginAvailable(login);

        // Then
        assertTrue(result);

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testIsLoginAvailable_NotAvailable() {
        // Given
        String login = "joao123";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.of(usuario));

        // When
        boolean result = usuarioDomainService.isLoginAvailable(login);

        // Then
        assertFalse(result);

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Given
        String login = "joao123";
        String password = "hashedPassword";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.of(usuario));

        // When
        boolean result = usuarioDomainService.authenticateUser(login, password);

        // Then
        assertTrue(result);

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        // Given
        String login = "joao123";
        String password = "wrongPassword";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.of(usuario));

        // When
        boolean result = usuarioDomainService.authenticateUser(login, password);

        // Then
        assertFalse(result);

        verify(usuarioRepository).findByLogin(login);
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        // Given
        String login = "nonexistent";
        String password = "password";
        when(usuarioRepository.findByLogin(login)).thenReturn(Optional.empty());

        // When
        boolean result = usuarioDomainService.authenticateUser(login, password);

        // Then
        assertFalse(result);

        verify(usuarioRepository).findByLogin(login);
    }
} 