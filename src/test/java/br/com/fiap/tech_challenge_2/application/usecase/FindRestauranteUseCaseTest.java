package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.FindRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestauranteUseCaseTest {

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @Mock
    private RestauranteMapper restauranteMapper;

    @InjectMocks
    private FindRestauranteUseCaseImpl findRestauranteUseCase;

    private RestauranteRequest request;
    private Restaurante domainRestaurante;

    private TipoUsuarioDTO tipoUsuarioDTO;
    private Usuario domainDono;
    private UsuarioResponse donoDTO;
    private Endereco domainEndereco;
    private EnderecoDTO enderecoDTO;
    private RestauranteResponse restauranteResponse;

    @BeforeEach
    void setUp() {
        domainEndereco = new Endereco(1L,"Rua do Restaurante", "100", "", "Centro", "São Paulo", "SP", "01234567");
        enderecoDTO = new EnderecoDTO("Rua do Restaurante", "100", "", "Centro", "São Paulo", "SP", "01234567");

        domainDono = new Usuario();
        domainDono.setId(1L);
        domainDono.setNome("João Silva");
        domainDono.setEmail("joao@email.com");
        domainDono.setLogin("joao123");

        tipoUsuarioDTO = new TipoUsuarioDTO(1L, "CLIENTE");

        donoDTO = new UsuarioResponse(1L, "João Silva", tipoUsuarioDTO, "joao@email.com", "joao123", null, LocalDate.now());

        domainRestaurante = new Restaurante();
        domainRestaurante.setId(1L);
        domainRestaurante.setNome("Restaurante Teste");
        domainRestaurante.setTipoCozinha("Italiana");
        domainRestaurante.setHorarioFuncionamento("12:00-22:00");
        domainRestaurante.setEndereco(domainEndereco);
        domainRestaurante.setDono(domainDono);

        restauranteResponse = new RestauranteResponse(1L, "Restaurante Teste",  enderecoDTO,"Italiana", "12:00-22:00", donoDTO, null);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Restaurante> restaurantes = List.of(domainRestaurante);
        when(restauranteDomainService.findAllRestaurantes()).thenReturn(restaurantes);
        when(restauranteMapper.toResponse(domainRestaurante)).thenReturn(restauranteResponse);

        // When
        Set<RestauranteResponse> result = findRestauranteUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restauranteMapper.toResponse(domainRestaurante), result.iterator().next());
        
        verify(restauranteDomainService).findAllRestaurantes();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(restauranteDomainService.findAllRestaurantes()).thenReturn(List.of());

        // When
        Set<RestauranteResponse> result = findRestauranteUseCase.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restauranteDomainService).findAllRestaurantes();
    }

    @Test
    void testFindById_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(domainRestaurante));
        when(restauranteMapper.toResponse(domainRestaurante)).thenReturn(restauranteResponse);

        // When
        RestauranteResponse result = findRestauranteUseCase.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(restauranteMapper.toResponse(domainRestaurante), result);
        
        verify(restauranteDomainService).findRestauranteById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(restauranteDomainService.findRestauranteById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            findRestauranteUseCase.findById(999L);
        });
        
        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());
        
        verify(restauranteDomainService).findRestauranteById(999L);
    }

    @Test
    void testFindById_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            findRestauranteUseCase.findById(null);
        });
        
        assertEquals("ID cannot be null", exception.getMessage());
        
        verify(restauranteDomainService, never()).findRestauranteById(any());
    }
} 