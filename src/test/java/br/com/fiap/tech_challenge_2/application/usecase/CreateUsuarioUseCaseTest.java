package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
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
import br.com.fiap.tech_challenge_2.application.service.TipoUsuarioService;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;

@ExtendWith(MockitoExtension.class)
class CreateUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TipoUsuarioService tipoUsuarioService;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    @InjectMocks
    private CreateUsuarioUseCaseImpl createUsuarioUseCase;

    private UsuarioRequest validRequest;
    private UsuarioResponse expectedResponse;
    private Usuario domainUsuario;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        
        validRequest = new UsuarioRequest("John Doe", "john@email.com", 1l, "johndoe", "password123", null);
        
        domainUsuario = new Usuario();
        domainUsuario.setId(1L);
        domainUsuario.setNome("John Doe");
        domainUsuario.setEmail("john@email.com");
        domainUsuario.setLogin("johndoe");
        domainUsuario.setTipoUsuario(tipoUsuario);

        expectedResponse = new UsuarioResponse(1L, "John Doe", tipoUsuarioDTO, "john@email.com", "johndoe", null, null);

        // Mock para tipoUsuarioService e tipoUsuarioMapper
        lenient().when(tipoUsuarioService.findById(1L)).thenReturn(tipoUsuarioDTO);
        lenient().when(tipoUsuarioMapper.toEntity(tipoUsuarioDTO)).thenReturn(tipoUsuario);
    }

    @Test
    void testExecute_Success() {
        // Given
        when(usuarioDomainService.isLoginAvailable(any())).thenReturn(true);
        when(passwordHasher.hashPassword(any())).thenReturn("hashedPassword");
        when(usuarioMapper.toEntity(any(UsuarioRequest.class))).thenReturn(domainUsuario);
        when(usuarioDomainService.createUser(any(Usuario.class))).thenReturn(domainUsuario);
        when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(expectedResponse);

        // When
        UsuarioResponse result = createUsuarioUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());
        assertEquals(expectedResponse.email(), result.email());
        assertEquals(expectedResponse.login(), result.login());
        assertEquals(expectedResponse.tipoUsuario().nome(), result.tipoUsuario().nome());

        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(passwordHasher).hashPassword("password123");
        verify(usuarioMapper).toEntity(validRequest);
        verify(usuarioDomainService).createUser(domainUsuario);
        verify(usuarioMapper).toResponse(domainUsuario);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            createUsuarioUseCase.execute(null);
        });

        verify(usuarioDomainService, never()).createUser(any());
    }

    @Test
    void testExecute_WithDuplicateLogin() {
        // Given
        when(usuarioDomainService.isLoginAvailable(any())).thenReturn(false);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            createUsuarioUseCase.execute(validRequest);
        });

        verify(usuarioDomainService).isLoginAvailable("johndoe");
        verify(usuarioDomainService, never()).createUser(any());
    }
} 