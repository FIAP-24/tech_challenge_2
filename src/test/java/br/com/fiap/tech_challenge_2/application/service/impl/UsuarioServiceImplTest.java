package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
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
    private Usuario domainUsuario;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;
    private UsuarioEditRequest usuarioEditRequest;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");

        usuarioRequest = new UsuarioRequest("John Doe", "john@email.com", 1l, "johndoe", "password123", null);

        domainUsuario = new Usuario();
        domainUsuario.setId(1L);
        domainUsuario.setNome("John Doe");
        domainUsuario.setEmail("john@email.com");
        domainUsuario.setLogin("johndoe");
        domainUsuario.setTipoUsuario(tipoUsuario);
        domainUsuario.setDataUpdate(LocalDate.now());
        enderecoDTO = new EnderecoDTO("Rua 123", "10", null, "Jacana", "Sao Paulo", "SP", "00012010");

        usuarioEditRequest = new UsuarioEditRequest("John Doe", "john@email.com", 1L, "johndoe", enderecoDTO);
    }

    @Test
    void testSave_Success() {
        // Given
        when(usuarioDomainService.isLoginAvailable(any())).thenReturn(true);
        when(passwordHasher.hashPassword(any())).thenReturn("hashedPassword");
        when(usuarioMapper.toEntity(any(UsuarioRequest.class))).thenReturn(domainUsuario);
        when(usuarioDomainService.createUser(any(Usuario.class))).thenReturn(domainUsuario);

        // When
        Usuario result = usuarioService.save(usuarioRequest);

        // Then
        assertNotNull(result);
        assertEquals(domainUsuario.getId(), result.getId());
        assertEquals(domainUsuario.getNome(), result.getNome());
        assertEquals(domainUsuario.getEmail(), result.getEmail());
        assertEquals(domainUsuario.getLogin(), result.getLogin());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.getTipoUsuario().getNome());

        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(passwordHasher).hashPassword("password123");
        verify(usuarioMapper).toEntity(usuarioRequest);
        verify(usuarioDomainService).createUser(domainUsuario);
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

        // When
        Usuario result = usuarioService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(domainUsuario.getId(), result.getId());
        assertEquals(domainUsuario.getNome(), result.getNome());
        assertEquals(domainUsuario.getEmail(), result.getEmail());
        assertEquals(domainUsuario.getLogin(), result.getLogin());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.getTipoUsuario().getNome());

        verify(usuarioDomainService).findUserById(1L);
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
        when(usuarioDomainService.findAllUsers()).thenReturn(domainUsuarios);

        // When
        Set<Usuario> result = usuarioService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(usuarioDomainService).findAllUsers();
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainUsuario));
        when(usuarioDomainService.updateUser(any(Usuario.class))).thenReturn(domainUsuario);

        // When
        Usuario result = usuarioService.update(1L, usuarioEditRequest);

        // Then
        assertNotNull(result);
        assertEquals(domainUsuario.getId(), result.getId());
        assertEquals(domainUsuario.getNome(), result.getNome());
        assertEquals(domainUsuario.getEmail(), result.getEmail());
        assertEquals(domainUsuario.getLogin(), result.getLogin());
        assertEquals(domainUsuario.getTipoUsuario().getNome(), result.getTipoUsuario().getNome());
        assertEquals(domainUsuario.getEndereco(), result.getEndereco());

        verify(usuarioDomainService).findUserById(1L);
    }

    @Test
    void testDelete_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainUsuario));
        doNothing().when(usuarioDomainService).deleteUser(1L);

        // When
        assertDoesNotThrow(() -> usuarioService.delete(1L));

        // Then
        verify(usuarioDomainService).findUserById(1L);
        verify(usuarioDomainService).deleteUser(1L);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> usuarioService.delete(1L));

        assertEquals("Usuário não encontrado com id: 1", exception.getMessage());

        verify(usuarioDomainService, atLeast(1)).findUserById(1L);
    }
} 