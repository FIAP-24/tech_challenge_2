package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.*;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
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
class RestauranteServiceImplTest {

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @Mock
    private RestauranteMapper restauranteMapper;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    @Mock
    private UsuarioDomainService usuarioDomainService;

    private Usuario domainDono;
    private TipoUsuario tipoUsuario;
    private RestauranteRequest restauranteRequest;
    private EnderecoDTO enderecoDTO;
    private Endereco domainEndereco;
    private Restaurante domainRestaurante;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "CLIENTE");

        domainDono = new Usuario();
        domainDono.setId(1L);
        domainDono.setNome("John Doe");
        domainDono.setEmail("john@email.com");
        domainDono.setLogin("johndoe");
        domainDono.setTipoUsuario(tipoUsuario);
        domainDono.setDataUpdate(LocalDate.now());

        enderecoDTO = new EnderecoDTO("Rua 123", "10", null, "Jacana", "Sao Paulo", "SP", "00012010");
        domainEndereco = new Endereco(1L,  "Rua 123", "10", null, "Jacana", "Sao Paulo", "SP", "00012010");

        domainRestaurante = new Restaurante();
        domainRestaurante.setNome("Restaurante Teste");
        domainRestaurante.setEndereco(domainEndereco);
        domainRestaurante.setTipoCozinha("Cozinha Teste");
        domainRestaurante.setHorarioFuncionamento("10:00-15:00");
        domainRestaurante.setDono(domainDono);

        restauranteRequest = new RestauranteRequest("Restaurante Teste", enderecoDTO, "Cozinha Teste", "10:00-15:00", 1L);
    }

    @Test
    void testSave_Success() {
        // Given
        when(restauranteDomainService.isRestauranteNameAvailable(any())).thenReturn(true);
        when(restauranteMapper.toEntity(any(RestauranteRequest.class))).thenReturn(domainRestaurante);
        when(restauranteDomainService.createRestaurante(any(Restaurante.class))).thenReturn(domainRestaurante);

        // When
        Restaurante result = restauranteService.save(restauranteRequest);

        // Then
        assertNotNull(result);
        assertEquals(domainRestaurante.getId(), result.getId());
        assertEquals(domainRestaurante.getNome(), result.getNome());
        assertEquals(domainRestaurante.getTipoCozinha(), result.getTipoCozinha());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.getHorarioFuncionamento());
        assertEquals(domainRestaurante.getEndereco(), result.getEndereco());
        assertEquals(domainRestaurante.getDono().getId(), result.getDono().getId());

        verify(restauranteDomainService).isRestauranteNameAvailable("Restaurante Teste");
        verify(restauranteMapper).toEntity(restauranteRequest);
        verify(restauranteDomainService).createRestaurante(domainRestaurante);
    }

    @Test
    void testSave_WithNullRequest() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            restauranteService.save(null);
        });

        verify(restauranteDomainService, never()).createRestaurante(any());
    }

    @Test
    void testSave_WithDuplicateLogin() {
        // Given
        when(restauranteDomainService.isRestauranteNameAvailable(any())).thenReturn(false);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            restauranteService.save(restauranteRequest);
        });

        verify(restauranteDomainService).isRestauranteNameAvailable("Restaurante Teste");
        verify(restauranteDomainService, never()).createRestaurante(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(domainRestaurante));

        // When
        Restaurante result = restauranteService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(domainRestaurante.getId(), result.getId());
        assertEquals(domainRestaurante.getNome(), result.getNome());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.getHorarioFuncionamento());
        assertEquals(domainRestaurante.getTipoCozinha(), result.getTipoCozinha());
        assertEquals(domainRestaurante.getDono().getId(), result.getDono().getId());

        verify(restauranteDomainService).findRestauranteById(1L);
    }

    @Test
    void testFindById_WithNullId() {
        // Given
        when(restauranteDomainService.findRestauranteById(null)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.findById(null);
        });

        verify(restauranteDomainService).findRestauranteById(null);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(restauranteDomainService.findRestauranteById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.findById(999L);
        });

        verify(restauranteDomainService).findRestauranteById(999L);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Restaurante> domainRestaurantes = List.of(domainRestaurante);
        when(restauranteDomainService.findAllRestaurantes()).thenReturn(domainRestaurantes);

        // When
        Set<Restaurante> result = restauranteService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(restauranteDomainService).findAllRestaurantes();
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(domainRestaurante));
        when(restauranteDomainService.updateRestaurante(any(Restaurante.class))).thenReturn(domainRestaurante);
        when(usuarioDomainService.findUserById(1L)).thenReturn(Optional.of(domainDono));

        // When
        Restaurante result = restauranteService.update(1L, restauranteRequest);

        // Then
        assertNotNull(result);
        assertEquals(domainRestaurante.getId(), result.getId());
        assertEquals(domainRestaurante.getNome(), result.getNome());
        assertEquals(domainRestaurante.getTipoCozinha(), result.getTipoCozinha());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.getHorarioFuncionamento());
        assertEquals(domainRestaurante.getDono().getId(), result.getDono().getId());
        assertEquals(domainRestaurante.getEndereco(), result.getEndereco());

        verify(restauranteDomainService).findRestauranteById(1L);
    }

    @Test
    void testDelete_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(domainRestaurante));
        doNothing().when(restauranteDomainService).deleteRestaurante(1L);

        // When
        assertDoesNotThrow(() -> restauranteService.delete(1L));

        // Then
        verify(restauranteDomainService).findRestauranteById(1L);
        verify(restauranteDomainService).deleteRestaurante(1L);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> restauranteService.delete(1L));

        assertEquals("Restaurante não encontrado com id: 1", exception.getMessage());

        verify(restauranteDomainService, atLeast(1)).findRestauranteById(1L);
    }
} 