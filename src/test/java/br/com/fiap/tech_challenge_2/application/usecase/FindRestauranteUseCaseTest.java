package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.FindRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestauranteUseCaseTest {

    @Mock
    private RestauranteDomainService restauranteDomainService;

    @InjectMocks
    private FindRestauranteUseCaseImpl findRestauranteUseCase;

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua do Restaurante");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");
        
        Usuario dono = new Usuario();
        dono.setId(1L);
        dono.setNome("João Silva");
        dono.setEmail("joao@email.com");
        dono.setLogin("joao123");
        
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("12:00-22:00");
        restaurante.setEndereco(endereco);
        restaurante.setDono(dono);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Restaurante> restaurantes = List.of(restaurante);
        when(restauranteDomainService.findAllRestaurantes()).thenReturn(restaurantes);

        // When
        List<Restaurante> result = findRestauranteUseCase.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurante, result.get(0));
        
        verify(restauranteDomainService).findAllRestaurantes();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(restauranteDomainService.findAllRestaurantes()).thenReturn(List.of());

        // When
        List<Restaurante> result = findRestauranteUseCase.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restauranteDomainService).findAllRestaurantes();
    }

    @Test
    void testFindById_Success() {
        // Given
        when(restauranteDomainService.findRestauranteById(1L)).thenReturn(Optional.of(restaurante));

        // When
        Restaurante result = findRestauranteUseCase.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(restaurante, result);
        
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