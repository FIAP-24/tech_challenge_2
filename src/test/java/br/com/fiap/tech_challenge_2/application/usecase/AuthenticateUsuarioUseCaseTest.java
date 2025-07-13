package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.usecase.impl.AuthenticateUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
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
class AuthenticateUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private AuthenticateUsuarioUseCaseImpl authenticateUsuarioUseCase;

    private Usuario usuario;
    private UsuarioLoginRequest request;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senhaHashada");
        usuario.setDataUpdate(LocalDate.now());
        
        request = new UsuarioLoginRequest("joao123", "senha123");
    }

    @Test
    void testExecute_Success() {
        // Given
        when(usuarioDomainService.findUserByLogin("joao123")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("senha123", "senhaHashada")).thenReturn(true);

        // When
        boolean result = authenticateUsuarioUseCase.execute(request);

        // Then
        assertTrue(result);
        
        verify(usuarioDomainService).findUserByLogin("joao123");
        verify(passwordHasher).verifyPassword("senha123", "senhaHashada");
    }

    @Test
    void testExecute_LoginNotFound() {
        // Given
        when(usuarioDomainService.findUserByLogin("usuarioinexistente")).thenReturn(Optional.empty());

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("usuarioinexistente", "senha123"));
        });
        
        assertEquals("Login não encontrado", exception.getMessage());
        
        verify(usuarioDomainService).findUserByLogin("usuarioinexistente");
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_WrongPassword() {
        // Given
        when(usuarioDomainService.findUserByLogin("joao123")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("senhaErrada", "senhaHashada")).thenReturn(false);

        // When & Then
        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("joao123", "senhaErrada"));
        });
        
        assertEquals("Senha incorreta", exception.getMessage());
        
        verify(usuarioDomainService).findUserByLogin("joao123");
        verify(passwordHasher).verifyPassword("senhaErrada", "senhaHashada");
    }

    @Test
    void testExecute_NullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(null);
        });
        
        assertEquals("Request cannot be null", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_NullLogin() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest(null, "senha123"));
        });
        
        assertEquals("Login is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_EmptyLogin() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("", "senha123"));
        });
        
        assertEquals("Login is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_BlankLogin() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("   ", "senha123"));
        });
        
        assertEquals("Login is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_NullSenha() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("joao123", null));
        });
        
        assertEquals("Senha is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_EmptySenha() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("joao123", ""));
        });
        
        assertEquals("Senha is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }

    @Test
    void testExecute_BlankSenha() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateUsuarioUseCase.execute(new UsuarioLoginRequest("joao123", "   "));
        });
        
        assertEquals("Senha is required", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserByLogin(any());
        verify(passwordHasher, never()).verifyPassword(any(), any());
    }
} 