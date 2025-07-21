package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.service.TipoUsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.impl.UpdateUsuarioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUsuarioUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TipoUsuarioService tipoUsuarioService;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    @InjectMocks
    private UpdateUsuarioUseCaseImpl updateUsuarioUseCase;

    private UsuarioEditRequest request;
    private UsuarioResponse usuarioResponse;
    private Usuario domainUsuario;
    private EnderecoDTO enderecoDTO;
    private Endereco enderecoDomain;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Nova", "456", "Apto 2", "Vila Nova", "São Paulo", "SP", "04567890");
        enderecoDomain = new Endereco(1L, "Rua Nova", "456", "Apto 2", "Vila Nova", "São Paulo", "SP", "04567890");
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        
        request = new UsuarioEditRequest("João Silva Atualizado", "joao.novo@email.com", 1l, "novaSenha123", enderecoDTO);
        
        domainUsuario = new Usuario();
        domainUsuario.setId(1L);
        domainUsuario.setNome("João Silva Atualizado");
        domainUsuario.setEmail("joao.novo@email.com");
        domainUsuario.setLogin("joao123");
        domainUsuario.setTipoUsuario(tipoUsuario);
        domainUsuario.setDataUpdate(LocalDate.now());

        usuarioResponse = new UsuarioResponse(1L, "João Silva Atualizado", tipoUsuarioDTO, "joao.novo@email.com", "joao123", enderecoDTO, LocalDate.now());
    }

    @Test
    void testExecute_Success() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainUsuario));
        when(passwordHasher.hashPassword(any())).thenReturn("hashedNewPassword");
        when(enderecoMapper.toEndereco(any(EnderecoDTO.class))).thenReturn(enderecoDomain);
        when(tipoUsuarioService.findById(1L)).thenReturn(tipoUsuarioDTO);
        when(tipoUsuarioMapper.toEntity(tipoUsuarioDTO)).thenReturn(tipoUsuario);
        when(usuarioDomainService.updateUser(any(Usuario.class))).thenReturn(domainUsuario);
        when(usuarioMapper.toResponse(any(Usuario.class))).thenReturn(usuarioResponse);

        // When
        UsuarioResponse result = updateUsuarioUseCase.execute(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(usuarioResponse.id(), result.id());
        assertEquals(usuarioResponse.nome(), result.nome());
        assertEquals(usuarioResponse.email(), result.email());
        assertEquals(usuarioResponse.login(), result.login());
        assertEquals(usuarioResponse.tipoUsuario().nome(), result.tipoUsuario().nome());

        verify(usuarioDomainService).findUserById(1L);
        verify(passwordHasher).hashPassword("novaSenha123");
        verify(enderecoMapper).toEndereco(enderecoDTO);
        verify(tipoUsuarioService).findById(1L);
        verify(tipoUsuarioMapper).toEntity(tipoUsuarioDTO);
        verify(usuarioDomainService).updateUser(domainUsuario);
        verify(usuarioMapper).toResponse(domainUsuario);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            updateUsuarioUseCase.execute(1L, null);
        });

        verify(usuarioDomainService, never()).updateUser(any());
    }

    @Test
    void testExecute_WithNullId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            updateUsuarioUseCase.execute(null, request);
        });

        verify(usuarioDomainService, never()).updateUser(any());
    }
} 