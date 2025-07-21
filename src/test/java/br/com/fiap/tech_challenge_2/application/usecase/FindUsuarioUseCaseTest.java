package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.FindUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private FindUsuarioUseCaseImpl findUsuarioUseCase;

    private Usuario domainUsuario;
    private UsuarioResponse usuarioResponse;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        
        domainUsuario = new Usuario();
        domainUsuario.setId(1L);
        domainUsuario.setNome("João Silva");
        domainUsuario.setEmail("joao@email.com");
        domainUsuario.setLogin("joao123");
        domainUsuario.setTipoUsuario(tipoUsuario);
        domainUsuario.setDataUpdate(LocalDate.now());

        usuarioResponse = new UsuarioResponse(1L, "João Silva", tipoUsuarioDTO, "joao@email.com", "joao123", null, LocalDate.now());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainUsuario));
        when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = findUsuarioUseCase.findById(1L);

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
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            findUsuarioUseCase.findById(null);
        });

        verify(usuarioDomainService, never()).findUserById(any());
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Usuario> domainUsuarios = List.of(domainUsuario);
        when(usuarioDomainService.findAllUsers()).thenReturn(domainUsuarios);

        // When
        Set<UsuarioResponse> result = findUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(usuarioDomainService).findAllUsers();
        verify(usuarioMapper).toResponse(domainUsuario);
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(usuarioDomainService.findAllUsers()).thenReturn(List.of());

        // When
        Set<UsuarioResponse> result = findUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(usuarioDomainService).findAllUsers();
    }
} 