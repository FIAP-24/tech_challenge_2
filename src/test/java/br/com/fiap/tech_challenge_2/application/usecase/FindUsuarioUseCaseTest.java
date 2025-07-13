package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.FindUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private FindUsuarioUseCaseImpl findUsuarioUseCase;

    private Usuario usuario;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 1");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setEndereco(endereco);
        usuario.setDataUpdate(LocalDate.now());
        
        usuarioResponse = new UsuarioResponse(1L, "João Silva", Perfil.CLIENTE, "joao@email.com", "joao123", null, LocalDate.now());
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Usuario> usuarios = List.of(usuario);
        when(usuarioDomainService.findAllUsers()).thenReturn(usuarios);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        Set<UsuarioResponse> result = findUsuarioUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(usuarioResponse));
        
        verify(usuarioDomainService).findAllUsers();
        verify(usuarioMapper).toResponse(usuario);
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
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = findUsuarioUseCase.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse, result);
        
        verify(usuarioDomainService).findUserById(1L);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(usuarioDomainService.findUserById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            findUsuarioUseCase.findById(999L);
        });
        
        assertEquals("Usuário não encontrado com id: 999", exception.getMessage());
        
        verify(usuarioDomainService).findUserById(999L);
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testFindById_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findUsuarioUseCase.findById(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserById(any());
        verify(usuarioMapper, never()).toResponse(any());
    }
} 