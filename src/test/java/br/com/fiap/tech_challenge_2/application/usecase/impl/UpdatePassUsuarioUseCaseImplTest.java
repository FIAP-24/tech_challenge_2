package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditPassRequest;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UpdatePassUsuarioUseCaseImplTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UpdatePassUsuarioUseCaseImpl updatePassUsuarioUseCase;

    private Usuario usuario;
    private UsuarioEditPassRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setLogin("testuser");
        usuario.setSenha("hashedOldPassword");

        validRequest = new UsuarioEditPassRequest(
                "testuser",
                "oldPassword",
                "newPassword",
                "newPassword"
        );
    }

    @Test
    void execute_QuandoTodosOsDadosEstaoCorretos_DeveAtualizarSenha() {
        // Arrange
        when(usuarioDomainService.findUserByLogin("testuser")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("oldPassword", "hashedOldPassword")).thenReturn(true);
        when(passwordHasher.hashPassword("newPassword")).thenReturn("hashedNewPassword");

        // Act
        boolean result = updatePassUsuarioUseCase.execute(validRequest);

        // Assert
        assertTrue(result);
        verify(usuarioDomainService).updateUser(usuario);
        assertEquals("hashedNewPassword", usuario.getSenha());
    }

    @Test
    void execute_QuandoRequestNulo_DeveLancarException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updatePassUsuarioUseCase.execute(null)
        );
        assertEquals("Request cannot be null", exception.getMessage());
        verify(usuarioDomainService, never()).findUserByLogin(any());
    }

    @Test
    void execute_QuandoLoginVazio_DeveLancarException() {
        // Arrange
        UsuarioEditPassRequest requestComLoginVazio = new UsuarioEditPassRequest(
                "  ",
                "oldPassword",
                "newPassword",
                "newPassword"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updatePassUsuarioUseCase.execute(requestComLoginVazio)
        );
        assertEquals("Login is required", exception.getMessage());
        verify(usuarioDomainService, never()).findUserByLogin(any());
    }

    @Test
    void execute_QuandoSenhasNovasNaoConferem_DeveLancarException() {
        // Arrange
        UsuarioEditPassRequest requestComSenhasDiferentes = new UsuarioEditPassRequest(
                "testuser",
                "oldPassword",
                "newPassword1",
                "newPassword2"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updatePassUsuarioUseCase.execute(requestComSenhasDiferentes)
        );
        assertEquals("As senhas novas não são iguais", exception.getMessage());
        verify(usuarioDomainService, never()).findUserByLogin(any());
    }

    @Test
    void execute_QuandoUsuarioNaoEncontrado_DeveLancarException() {
        // Arrange
        when(usuarioDomainService.findUserByLogin("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> updatePassUsuarioUseCase.execute(validRequest)
        );
        assertEquals("Login não encontrado", exception.getMessage());
        verify(usuarioDomainService, never()).updateUser(any());
    }

    @Test
    void execute_QuandoSenhaAtualIncorreta_DeveLancarException() {
        // Arrange
        when(usuarioDomainService.findUserByLogin("testuser")).thenReturn(Optional.of(usuario));
        when(passwordHasher.verifyPassword("oldPassword", "hashedOldPassword")).thenReturn(false);

        // Act & Assert
        AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> updatePassUsuarioUseCase.execute(validRequest)
        );
        assertEquals("Senha incorreta", exception.getMessage());
        verify(usuarioDomainService, never()).updateUser(any());
    }


}