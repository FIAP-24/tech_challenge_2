package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RestauranteMapperTest {
    @InjectMocks
    private RestauranteMapperImpl restauranteMapper;

    private Restaurante domainRestaurante;
    private RestauranteRequest restauranteRequest;
    private Endereco domainEndereco;
    private EnderecoDTO enderecoDTO;
    private Usuario domainDono;
    private TipoUsuario domainTipoUsuario;
    private List<ItemCardapio> domainCardapio;

    @BeforeEach
    void setUp() {
        enderecoDTO = new EnderecoDTO("Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");
        domainEndereco = new Endereco(1L, "Rua Teste", "123", "Apto 1", "Centro", "São Paulo", "SP", "01234567");

        domainTipoUsuario = new TipoUsuario(1L, "CLIENTE");
        domainDono = new Usuario(1L, "João Silva", "joao@email.com", "joao123", "senha123", LocalDate.now(), domainEndereco, domainTipoUsuario);
        domainCardapio = new ArrayList<ItemCardapio>();

        domainRestaurante = new Restaurante(1L, "Restaurante Teste", domainEndereco, "Italiana", "12:00-22:00", domainDono, domainCardapio);
        restauranteRequest = new RestauranteRequest("Restaurante Teste", enderecoDTO, "Italiana", "12:00-22:00", 1L);
    }

    @Test
    void testToResponse_Success() {
        // When
        RestauranteResponse result = restauranteMapper.toResponse(domainRestaurante);

        // Then
        assertNotNull(result);
        assertEquals(domainRestaurante.getId(), result.id());
        assertEquals(domainRestaurante.getDono().getId(), result.dono().id());
        assertEquals(domainRestaurante.getTipoCozinha(), result.tipoCozinha());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.horarioFuncionamento());

        // Endereco é mapeado automaticamente
        assertNotNull(result.endereco());
        assertEquals(domainEndereco.getLogradouro(), result.endereco().logradouro());
    }

    @Test
    void testToResponse_WithNullRestaurante() {
        // When
        RestauranteResponse result = restauranteMapper.toResponse(null);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_Success() {
        // When
        Restaurante result = restauranteMapper.toEntity(restauranteRequest);

        // Then
        assertNotNull(result);
        assertEquals(restauranteRequest.nome(), result.getNome());
        assertEquals(restauranteRequest.horarioFuncionamento(), result.getHorarioFuncionamento());
        assertEquals(restauranteRequest.tipoCozinha(), result.getTipoCozinha());
        // Endereco é mapeado automaticamente
        assertNotNull(result.getEndereco());
        assertEquals(enderecoDTO.logradouro(), result.getEndereco().getLogradouro());
    }

    @Test
    void testToEntity_WithNullRequest() {
        // When
        Restaurante result = restauranteMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_WithId() {
        // When
        Restaurante result = restauranteMapper.toEntity(restauranteRequest, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(restauranteRequest.nome(), result.getNome());
        assertEquals(restauranteRequest.horarioFuncionamento(), result.getHorarioFuncionamento());
        assertEquals(restauranteRequest.tipoCozinha(), result.getTipoCozinha());
    }

    @Test
    void testToResponseList_Success() {
        // Given
        List<Restaurante> domainRestaurantes = List.of(domainRestaurante);

        // When
        List<RestauranteResponse> result = restauranteMapper.toResponseList(domainRestaurantes);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domainRestaurante.getId(), result.get(0).id());
        assertEquals(domainRestaurante.getNome(), result.get(0).nome());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.get(0).horarioFuncionamento());
        assertEquals(domainRestaurante.getTipoCozinha(), result.get(0).tipoCozinha());
        assertEquals(domainRestaurante.getDono().getId(), result.get(0).dono().id());
    }

    @Test
    void testToResponseSet_Success() {
        // Given
        Set<Restaurante> domainRestaurantes = Set.of(domainRestaurante);

        // When
        Set<RestauranteResponse> result = restauranteMapper.toResponseSet(domainRestaurantes);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(domainRestaurante.getId(), result.iterator().next().id());
        assertEquals(domainRestaurante.getNome(), result.iterator().next().nome());
        assertEquals(domainRestaurante.getHorarioFuncionamento(), result.iterator().next().horarioFuncionamento());
        assertEquals(domainRestaurante.getTipoCozinha(), result.iterator().next().tipoCozinha());
        assertEquals(domainRestaurante.getDono().getId(), result.iterator().next().dono().id());
    }
}
