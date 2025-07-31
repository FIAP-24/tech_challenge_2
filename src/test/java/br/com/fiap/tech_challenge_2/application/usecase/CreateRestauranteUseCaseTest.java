package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
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
class CreateRestauranteUseCaseTest {

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @Mock
    private RestauranteMapper restauranteMapper;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CreateRestauranteUseCaseImpl createRestauranteUseCase;

    private RestauranteRequest validRequest;
    private RestauranteResponse expectedResponse;
    private Restaurante domainRestaurante;
    private Usuario dono;
    private UsuarioResponse donoDTO;
    private Endereco endereco;
    private TipoUsuarioDTO tipoUsuarioDTO;

    @BeforeEach
    void setUp() {
        EnderecoDTO enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        validRequest = new RestauranteRequest("Restaurante Teste UseCase", enderecoDTO, "Italiana", "Seg-Sex: 09:00-22:00", 1L);

        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");

        donoDTO = new UsuarioResponse(1L, "John Doe", tipoUsuarioDTO, "john@email.com", "johndoe", null, null);
        dono = new Usuario();
        dono.setId(1L);
        dono.setNome("João Silva");
        
        endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");

        expectedResponse = new RestauranteResponse(1L, "Restaurante Teste UseCase", enderecoDTO,  "Italiana", "Seg-Sex: 09:00-22:00", donoDTO, null);

        domainRestaurante = new Restaurante();
        domainRestaurante.setId(1L);
        domainRestaurante.setNome("Restaurante Teste UseCase");
        domainRestaurante.setTipoCozinha("Italiana");
        domainRestaurante.setHorarioFuncionamento("Seg-Sex: 09:00-22:00");
        domainRestaurante.setEndereco(endereco);
    }

    @Test
    void textExecute_Success() {
        // Given
        when(usuarioService.findById(1L)).thenReturn(dono);
        when(restauranteDomainService.isRestauranteNameAvailable(any())).thenReturn(true);
        when(restauranteMapper.toEntity(any(RestauranteRequest.class))).thenReturn(domainRestaurante);
        when(restauranteDomainService.createRestaurante(any(Restaurante.class))).thenReturn(domainRestaurante);
        when(restauranteMapper.toResponse(any(Restaurante.class))).thenReturn(expectedResponse);

        // When
        RestauranteResponse result = createRestauranteUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponse.id(), result.id());
        assertEquals(expectedResponse.nome(), result.nome());
        assertEquals(expectedResponse.tipoCozinha(), result.tipoCozinha());
        assertEquals(expectedResponse.horarioFuncionamento(), result.horarioFuncionamento());
        assertEquals(expectedResponse.endereco(), result.endereco());
        assertEquals(expectedResponse.dono().id(), result.dono().id());

        verify(restauranteDomainService).isRestauranteNameAvailable("Restaurante Teste UseCase");
        verify(restauranteMapper).toEntity(validRequest);
        verify(restauranteDomainService).createRestaurante(domainRestaurante);
        verify(restauranteMapper).toResponse(domainRestaurante);
    }

    @Test
    void testExecute_UsuarioNotFound() {
        // Given
        when(usuarioService.findById(1L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado com id: 1"));
        when(restauranteDomainService.isRestauranteNameAvailable(any())).thenReturn(true);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            createRestauranteUseCase.execute(validRequest);
        });

        verify(usuarioService).findById(1L);
        verify(restauranteDomainService, never()).createRestaurante(any());
    }

    @Test
    void testExecute_WithNullRequest() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            createRestauranteUseCase.execute(null);
        });

        verify(restauranteDomainService, never()).createRestaurante(any());
    }
} 