package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;
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
class RestauranteServiceImplTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    private RestauranteRequestDTO requestDTO;
    private Usuario dono;
    private Endereco endereco;
    private Restaurante restaurante;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        dono = new Usuario();
        dono.setId(1L);
        dono.setNome("João Silva");
        dono.setEmail("joao@email.com");
        dono.setLogin("joao123");
        dono.setDataUpdate(LocalDate.now());

        endereco = new Endereco();
        endereco.setLogradouro("Rua do Restaurante");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");

        enderecoDTO = new EnderecoDTO("Rua do Restaurante", "100", null, "Centro", "São Paulo", "SP", "01234567");
        requestDTO = new RestauranteRequestDTO("Restaurante Teste", enderecoDTO, "Italiana", "12:00-22:00", 1L);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("12:00-22:00");
        restaurante.setEndereco(endereco);
        restaurante.setDono(dono);
    }

    @Test
    void testCreate_Success() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(enderecoMapper.toEndereco(requestDTO.endereco())).thenReturn(endereco);
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);

        // When
        Restaurante result = restauranteService.create(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Restaurante Teste", result.getNome());
        assertEquals("Italiana", result.getTipoCozinha());
        assertEquals("12:00-22:00", result.getHorarioFuncionamento());
        assertEquals(dono, result.getDono());
        assertEquals(endereco, result.getEndereco());

        verify(usuarioRepository).findById(1L);
        verify(enderecoMapper).toEndereco(requestDTO.endereco());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    void testCreate_UsuarioNotFound() {
        // Given
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.create(new RestauranteRequestDTO("Restaurante", enderecoDTO, "Italiana", "12:00-22:00", 999L));
        });

        assertEquals("Usuário (dono) não encontrado com id: 999", exception.getMessage());

        verify(usuarioRepository).findById(999L);
        verify(enderecoMapper, never()).toEndereco(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        // When
        Restaurante result = restauranteService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(restaurante, result);

        verify(restauranteRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.findById(999L);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).findById(999L);
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Restaurante> restaurantes = List.of(restaurante);
        when(restauranteRepository.findAll()).thenReturn(restaurantes);

        // When
        List<Restaurante> result = restauranteService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restaurante, result.get(0));

        verify(restauranteRepository).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(restauranteRepository.findAll()).thenReturn(List.of());

        // When
        List<Restaurante> result = restauranteService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restauranteRepository).findAll();
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(enderecoMapper.toEndereco(requestDTO.endereco())).thenReturn(endereco);
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

        // When
        Restaurante result = restauranteService.update(1L, requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Restaurante Teste", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha());
        assertEquals("12:00-22:00", restaurante.getHorarioFuncionamento());
        assertEquals(dono, restaurante.getDono());
        assertEquals(endereco, restaurante.getEndereco());

        verify(restauranteRepository).findById(1L);
        verify(usuarioRepository).findById(1L);
        verify(enderecoMapper).toEndereco(requestDTO.endereco());
        verify(restauranteRepository).save(restaurante);
    }

    @Test
    void testUpdate_RestauranteNotFound() {
        // Given
        when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.update(999L, requestDTO);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).findById(999L);
        verify(usuarioRepository, never()).findById(any());
        verify(enderecoMapper, never()).toEndereco(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void testUpdate_UsuarioNotFound() {
        // Given
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.update(1L, new RestauranteRequestDTO("Restaurante", enderecoDTO, "Italiana", "12:00-22:00", 999L));
        });

        assertEquals("Usuário (dono) não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).findById(1L);
        verify(usuarioRepository).findById(999L);
        verify(enderecoMapper, never()).toEndereco(any());
        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        // Given
        when(restauranteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(restauranteRepository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> restauranteService.delete(1L));

        // Then
        verify(restauranteRepository).existsById(1L);
        verify(restauranteRepository).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(restauranteRepository.existsById(999L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.delete(999L);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(restauranteRepository).existsById(999L);
        verify(restauranteRepository, never()).deleteById(any());
    }
} 