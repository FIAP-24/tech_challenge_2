package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.usecase.impl.CreateRestauranteUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
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
    private UsuarioDomainService usuarioDomainService;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private CreateRestauranteUseCaseImpl createRestauranteUseCase;

    private RestauranteRequestDTO validRequest;
    private Usuario owner;
    private Endereco endereco;
    private Restaurante expectedRestaurante;

    @BeforeEach
    void setUp() {
        EnderecoDTO enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        validRequest = new RestauranteRequestDTO("Restaurante Teste UseCase", enderecoDTO, "Italiana", "Seg-Sex: 09:00-22:00", 1L);
        
        owner = new Usuario();
        owner.setId(1L);
        owner.setNome("João Silva");
        
        endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        
        expectedRestaurante = new Restaurante();
        expectedRestaurante.setId(1L);
        expectedRestaurante.setNome("Restaurante Teste UseCase");
        expectedRestaurante.setTipoCozinha("Italiana");
        expectedRestaurante.setHorarioFuncionamento("Seg-Sex: 09:00-22:00");
        expectedRestaurante.setDono(owner);
        expectedRestaurante.setEndereco(endereco);
    }

    @Test
    void shouldCreateRestauranteSuccessfully() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(java.util.Optional.of(owner));
        when(enderecoMapper.toEndereco(any(EnderecoDTO.class))).thenReturn(endereco);
        when(restauranteDomainService.createRestaurante(any(Restaurante.class))).thenReturn(expectedRestaurante);

        // When
        Restaurante result = createRestauranteUseCase.execute(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedRestaurante.getNome(), result.getNome());
        assertEquals(expectedRestaurante.getTipoCozinha(), result.getTipoCozinha());
        assertEquals(expectedRestaurante.getHorarioFuncionamento(), result.getHorarioFuncionamento());
        
        verify(usuarioDomainService).findUserById(1L);
        verify(enderecoMapper).toEndereco(any(EnderecoDTO.class));
        verify(restauranteDomainService).createRestaurante(any(Restaurante.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        // Given
        when(usuarioDomainService.findUserById(1L)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            createRestauranteUseCase.execute(validRequest);
        });
        
        verify(usuarioDomainService).findUserById(1L);
        verifyNoInteractions(enderecoMapper, restauranteDomainService);
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            createRestauranteUseCase.execute(null);
        });
        
        verifyNoInteractions(usuarioDomainService, enderecoMapper, restauranteDomainService);
    }
} 