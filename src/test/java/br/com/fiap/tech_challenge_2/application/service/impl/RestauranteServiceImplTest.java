package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
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
    private RestauranteRepository repository;

    @Mock
    private RestauranteMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    private RestauranteDTO restauranteDTO;
    private Restaurante restaurante;
    private Usuario dono;
    private Endereco endereco;
    private EnderecoDTO enderecoDTO;

    @BeforeEach
    void setUp() {
        dono = new Usuario();
        dono.setId(1L);
        dono.setNome("João Silva");
        dono.setEmail("joao@email.com");
        dono.setLogin("joao123");
        dono.setDataUpdate(LocalDate.now());

        enderecoDTO = new EnderecoDTO("Rua do Restaurante", "100", null, "Centro", "São Paulo", "SP", "01234567");

        endereco = new Endereco();
        endereco.setLogradouro("Rua do Restaurante");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");

        restauranteDTO = new RestauranteDTO("Restaurante Teste", enderecoDTO, "Italiana", "12:00-22:00", 1L);

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
        when(mapper.toEntity(restauranteDTO)).thenReturn(restaurante);
        when(repository.save(restaurante)).thenReturn(restaurante);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(dono));
        when(mapper.toDTO(restaurante)).thenReturn(restauranteDTO);

        // When
        RestauranteDTO result = restauranteService.create(restauranteDTO);

        // Then
        assertNotNull(result);
        assertEquals(restauranteDTO, result);

        verify(usuarioRepository).findById(1L);
        verify(mapper).toEntity(restauranteDTO);
        verify(repository).save(restaurante);
        verify(mapper).toDTO(restaurante);
    }

    @Test
    void testCreate_UsuarioNotFound() {
        // Given
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.create(new RestauranteDTO("Restaurante", enderecoDTO, "Italiana", "12:00-22:00", 999L));
        });

        assertEquals("Usuário (dono) não encontrado com id: 999", exception.getMessage());

        verify(usuarioRepository).findById(999L);
        verify(enderecoMapper, never()).toEndereco(any());
        verify(repository, never()).save(any());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(mapper.toDTO(restaurante)).thenReturn(restauranteDTO);

        // When
        RestauranteDTO result = restauranteService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(restauranteDTO, result);

        verify(repository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.findById(999L);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testFindAll_Success() {
        // Given
        List<Restaurante> restaurantes = List.of(restaurante);
        when(repository.findAll()).thenReturn(restaurantes);
        when(mapper.toDTO(restaurante)).thenReturn(restauranteDTO);


        // When
        List<RestauranteDTO> result = restauranteService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restauranteDTO, result.get(0));

        verify(repository).findAll();
        verify(mapper).toDTO(restaurante);
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<RestauranteDTO> result = restauranteService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testUpdate_Success() {
        // Given
        EnderecoDTO updateEnderecoDTO = new EnderecoDTO("Nova Rua do Restaurante", "101", "Novo Complemento", "Novo Bairro", "Belo Horizonte", "MG", "76543210");

        Endereco updateEndereco = new Endereco();
        updateEndereco.setLogradouro("Nova Rua do Restaurante");
        updateEndereco.setNumero("101");
        updateEndereco.setComplemento("Novo Complemento");
        updateEndereco.setBairro("Novo Bairro");
        updateEndereco.setCidade("Belo Horizonte");
        updateEndereco.setEstado("MG");
        updateEndereco.setCep("76543210");

        Usuario updateDono = new Usuario();
        updateDono.setId(2L);
        updateDono.setNome("Lucas Rangel");
        updateDono.setEmail("lucas@email.com");
        updateDono.setLogin("lucas123");
        updateDono.setDataUpdate(LocalDate.now());

        RestauranteDTO updateDTO = new RestauranteDTO("Novo Restaurante", updateEnderecoDTO, "Nova Culinaria", "11:00-20:00", 2L);

        when(repository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(updateDono));
        when(enderecoMapper.toEndereco(updateEnderecoDTO)).thenReturn(updateEndereco);
        when(repository.save(restaurante)).thenReturn(restaurante);
        when(mapper.toDTO(restaurante)).thenReturn(updateDTO);

        // When
        RestauranteDTO result = restauranteService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Novo Restaurante", restaurante.getNome());
        assertEquals("Nova Culinaria", restaurante.getTipoCozinha());
        assertEquals("11:00-20:00", restaurante.getHorarioFuncionamento());
        assertEquals(updateDono, restaurante.getDono());
        assertEquals(updateEndereco, restaurante.getEndereco());

        verify(repository).findById(1L);
        verify(usuarioRepository).findById(2L);
        verify(enderecoMapper).toEndereco(updateDTO.endereco());
        verify(repository).save(restaurante);
    }

    @Test
    void testUpdate_RestauranteNotFound() {
        // Given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.update(999L, restauranteDTO);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(999L);
        verify(usuarioRepository, never()).findById(any());
        verify(enderecoMapper, never()).toEndereco(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testUpdate_UsuarioNotFound() {
        // Given
        RestauranteDTO updateDTO = new RestauranteDTO("Restaurante", enderecoDTO, "Italiana", "12:00-22:00", 999L);
        when(repository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.update(1L, updateDTO);
        });

        assertEquals("Usuário (dono) não encontrado com id: 999", exception.getMessage());

        verify(repository).findById(1L);
        verify(usuarioRepository).findById(999L);
        verify(enderecoMapper, never()).toEndereco(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void testDelete_Success() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        assertDoesNotThrow(() -> restauranteService.delete(1L));

        // Then
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(repository.existsById(999L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            restauranteService.delete(999L);
        });

        assertEquals("Restaurante não encontrado com id: 999", exception.getMessage());

        verify(repository).existsById(999L);
        verify(repository, never()).deleteById(any());
    }
} 