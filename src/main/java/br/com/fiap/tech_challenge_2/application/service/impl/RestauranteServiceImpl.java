package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository repository;
    private final RestauranteMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoMapper enderecoMapper;

    @Override
    @Transactional
    public RestauranteDTO create(RestauranteDTO dto) {
        usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (dono) não encontrado com id: " + dto.donoId()));

        Restaurante restaurante = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(restaurante));
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RestauranteDTO update(Long id, RestauranteDTO dto) {
        Restaurante restaurante = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));

        Usuario dono = usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (dono) não encontrado com id: " + dto.donoId()));

        Endereco endereco = enderecoMapper.toEndereco(dto.endereco());

        restaurante.setNome(dto.nome());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        restaurante.setDono(dono);
        restaurante.setEndereco(endereco);
        return mapper.toDTO(repository.save(restaurante));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}