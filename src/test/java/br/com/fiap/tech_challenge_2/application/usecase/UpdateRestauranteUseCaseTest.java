package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.impl.UpdateRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.*;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateRestauranteUseCaseTest {

    @Mock
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private RestauranteMapper restauranteMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private UpdateRestauranteUseCaseImpl updateRestauranteUseCase;

    private RestauranteRequest request;
    private RestauranteResponse restauranteResponse;
    private Restaurante domainRestaurante;
    private Usuario domainDono;
    private UsuarioResponse donoResponse;
    private EnderecoDTO enderecoDTO;
    private Endereco domainEndereco;
    private TipoUsuarioDTO tipoUsuarioDTO;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Nova", "456", "Apto 2", "Vila Nova", "São Paulo", "SP", "04567890");
        domainEndereco = new Endereco(1L, "Rua Nova", "456", "Apto 2", "Vila Nova", "São Paulo", "SP", "04567890");
        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");

        request = new RestauranteRequest("Novo Restaurante", enderecoDTO, "Nova Cozinha", "08:00-16:00", 1L);

        domainDono = new Usuario();
        domainDono.setId(1L);
        domainDono.setNome("João Silva");
        domainDono.setEmail("joao@email.com");
        domainDono.setLogin("joao123");
        domainDono.setTipoUsuario(tipoUsuario);
        domainDono.setDataUpdate(LocalDate.now());

        domainRestaurante = new Restaurante();
        domainRestaurante.setId(1L);
        domainRestaurante.setNome("Novo Restaurante");
        domainRestaurante.setDono(domainDono);
        domainRestaurante.setEndereco(domainEndereco);
        domainRestaurante.setTipoCozinha("Nova Cozinha");
        domainRestaurante.setHorarioFuncionamento("08:00-16:00");

        donoResponse = new UsuarioResponse(1L, "João Silva", tipoUsuarioDTO, "joao@email.com", "joao123", enderecoDTO, LocalDate.now());

        restauranteResponse = new RestauranteResponse(1L, "Novo Restaurante", enderecoDTO, "Nova Cozinha", "08:00-16:00", donoResponse, null);
    }

    @Test
    void testExecute_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(domainRestaurante));
        when(enderecoMapper.toEndereco(any(EnderecoDTO.class))).thenReturn(domainEndereco);
        when(usuarioService.findById(1L)).thenReturn(domainDono);
        when(restauranteDomainService.updateRestaurante(any(Restaurante.class))).thenReturn(domainRestaurante);
        when(restauranteMapper.toResponse(any(Restaurante.class))).thenReturn(restauranteResponse);

        // When
        RestauranteResponse result = updateRestauranteUseCase.execute(1L, request);

        // Then
        assertNotNull(result);
        assertEquals(restauranteResponse.id(), result.id());
        assertEquals(restauranteResponse.nome(), result.nome());
        assertEquals(restauranteResponse.tipoCozinha(), result.tipoCozinha());
        assertEquals(restauranteResponse.horarioFuncionamento(), result.horarioFuncionamento());
        assertEquals(restauranteResponse.dono().id(), result.dono().id());
        assertEquals(restauranteResponse.endereco(), result.endereco());

        verify(restauranteDomainService).findRestauranteById(1L);
        verify(enderecoMapper).toEndereco(enderecoDTO);
        verify(usuarioService).findById(1L);
        verify(restauranteDomainService).updateRestaurante(domainRestaurante);
        verify(restauranteMapper).toResponse(domainRestaurante);
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            updateRestauranteUseCase.execute(1L, null);
        });

        verify(restauranteDomainService, never()).updateRestaurante(any());
    }

    @Test
    void testExecute_WithNullId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            updateRestauranteUseCase.execute(null, request);
        });

        verify(restauranteDomainService, never()).updateRestaurante(any());
    }
}
