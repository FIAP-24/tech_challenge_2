package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoToDtoMapper;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.infrastructure.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnderecoToDtoMapper enderecoToDTOMapper;

    @Override
    @Transactional
    public Restaurante create(RestauranteRequestDTO dto) {
        Usuario dono = usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (dono) não encontrado com id: " + dto.donoId()));

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.nome());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        restaurante.setEndereco(enderecoToDTOMapper.toEndereco(dto.endereco()));
        restaurante.setDono(dono);

        return restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurante findById(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> findAll() {
        return restauranteRepository.findAll();
    }

    @Override
    @Transactional
    public Restaurante update(Long id, RestauranteRequestDTO dto) {
        Restaurante restaurante = findById(id);
        Usuario dono = usuarioRepository.findById(dto.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (dono) não encontrado com id: " + dto.donoId()));

        restaurante.setNome(dto.nome());
        restaurante.setTipoCozinha(dto.tipoCozinha());
        restaurante.setHorarioFuncionamento(dto.horarioFuncionamento());
        restaurante.setEndereco(enderecoToDTOMapper.toEndereco(dto.endereco()));
        restaurante.setDono(dono);

        return restauranteRepository.save(restaurante);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurante não encontrado com id: " + id);
        }
        restauranteRepository.deleteById(id);
    }
}