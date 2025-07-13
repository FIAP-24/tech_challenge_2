package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
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
class CreateUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private CreateUsuarioUseCaseImpl createUsuarioUseCase;

    private UsuarioRequest validRequest;
    private Usuario domainUsuario;
    private Usuario savedUsuario;
    private UsuarioResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = new UsuarioRequest("John Doe", "john@email.com", Perfil.CLIENTE, "johndoe", "password123", null);
        domainUsuario = new Usuario();
        domainUsuario.setNome("John Doe");
        domainUsuario.setEmail("john@email.com");
        domainUsuario.setLogin("johndoe");
        
        savedUsuario = new Usuario();
        savedUsuario.setId(1L);
        savedUsuario.setNome("John Doe");
        savedUsuario.setEmail("john@email.com");
        savedUsuario.setLogin("johndoe");
        
        expectedResponse = new UsuarioResponse(1L, "John Doe", Perfil.CLIENTE, "john@email.com", "johndoe", null, null);
    }

    @Test
    void shouldCreateUsuarioSuccessfully() {
        // Given
        when(usuarioDomainService.isLoginAvailable("johndoe")).thenReturn(true);
        when(usuarioMapper.toEntity(validRequest)).thenReturn(domainUsuario);
        when(passwordHasher.hashPassword("password123")).thenReturn("hashedPassword");
        when(usuarioDomainService.createUser(any(Usuario.class))).thenReturn(savedUsuario);
        when(usuarioMapper.toResponse(savedUsuario)).thenReturn(expectedResponse);

        // When
        UsuarioResponse result = createUsuarioUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());
        assertEquals(expectedResponse.email(), result.email());
        assertEquals(expectedResponse.login(), result.login());
        
        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(usuarioMapper).toEntity(validRequest);
        verify(passwordHasher).hashPassword("password123");
        verify(usuarioDomainService).createUser(any(Usuario.class));
        verify(usuarioMapper).toResponse(savedUsuario);
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        // Given
        when(usuarioDomainService.isLoginAvailable("johndoe")).thenReturn(false);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            createUsuarioUseCase.execute(validRequest);
        });
        
        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verifyNoInteractions(usuarioMapper, passwordHasher);
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            createUsuarioUseCase.execute(null);
        });
        
        verifyNoInteractions(usuarioDomainService, usuarioMapper, passwordHasher);
    }
} 