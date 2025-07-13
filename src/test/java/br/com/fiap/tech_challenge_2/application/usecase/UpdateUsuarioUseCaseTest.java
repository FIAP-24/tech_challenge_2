package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.UpdateUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
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
class UpdateUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UpdateUsuarioUseCaseImpl updateUsuarioUseCase;

    private Usuario usuario;
    private UsuarioEditRequest request;
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
        
        EnderecoDTO enderecoDTO = new EnderecoDTO("Rua Nova", "456", "Apto 2", "Vila Nova", "Rio de Janeiro", "RJ", "20000000");
        request = new UsuarioEditRequest("João Silva Atualizado", "joao.novo@email.com", Perfil.CLIENTE, "novaSenha123", enderecoDTO);
        
        usuarioResponse = new UsuarioResponse(1L, "João Silva Atualizado", Perfil.CLIENTE, "joao.novo@email.com", "joao123", enderecoDTO, LocalDate.now());
    }

    @Test
    void testExecute_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        when(passwordHasher.hashPassword("novaSenha123")).thenReturn("senhaHashada");
        when(usuarioDomainService.updateUser(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = updateUsuarioUseCase.execute(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse, result);
        assertEquals("João Silva Atualizado", usuario.getNome());
        assertEquals("joao.novo@email.com", usuario.getEmail());
        assertEquals("senhaHashada", usuario.getSenha());
        
        verify(usuarioDomainService).findUserById(1L);
        verify(passwordHasher).hashPassword("novaSenha123");
        verify(usuarioDomainService).updateUser(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testExecute_UserNotFound() {
        // Given
        when(usuarioDomainService.findUserById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            updateUsuarioUseCase.execute(999L, request);
        });
        
        assertEquals("Usuário não encontrado com id: 999", exception.getMessage());
        
        verify(usuarioDomainService).findUserById(999L);
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService, never()).updateUser(any());
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateUsuarioUseCase.execute(null, request);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserById(any());
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService, never()).updateUser(any());
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_NullRequest() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateUsuarioUseCase.execute(1L, null);
        });
        
        assertEquals("Request cannot be null", exception.getMessage());
        
        verify(usuarioDomainService, never()).findUserById(any());
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService, never()).updateUser(any());
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    void testExecute_PartialUpdate() {
        // Given
        UsuarioEditRequest partialRequest = new UsuarioEditRequest("Novo Nome", null, null, null, null);
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioDomainService.updateUser(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = updateUsuarioUseCase.execute(1L, partialRequest);

        // Then
        assertNotNull(result);
        assertEquals("Novo Nome", usuario.getNome());
        // Email and password should remain unchanged
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        
        verify(usuarioDomainService).findUserById(1L);
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService).updateUser(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void testExecute_EmptyFields() {
        // Given
        UsuarioEditRequest emptyRequest = new UsuarioEditRequest("", "", null, "", null);
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioDomainService.updateUser(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = updateUsuarioUseCase.execute(1L, emptyRequest);

        // Then
        assertNotNull(result);
        // Fields should remain unchanged due to empty values
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        
        verify(usuarioDomainService).findUserById(1L);
        verify(passwordHasher, never()).hashPassword(any());
        verify(usuarioDomainService).updateUser(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }
} 