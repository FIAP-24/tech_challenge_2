package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {
    private final RestauranteDomainService restauranteDomainService;
    private final RestauranteMapper restauranteMapper;
    private final UsuarioDomainService usuarioDomainService;

    @Override
    @Transactional
    public Restaurante save(RestauranteRequest request) {
        if(!restauranteDomainService.isRestauranteNameAvailable(request.nome())){
            throw new DuplicateResourceException("Nome do restaurante já está em uso");
        }

        // Convert to domain entity
        Restaurante restaurante = restauranteMapper.toEntity(request);

        // Use domain service
        return restauranteDomainService.createRestaurante(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurante findById(Long id) {
        return restauranteDomainService.findRestauranteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Restaurante> findAll() {
        return restauranteDomainService.findAllRestaurantes().stream()
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Restaurante update(Long id, RestauranteRequest request) {
        Restaurante existingRestaurante = restauranteDomainService.findRestauranteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));

        updateRestauranteData(request, existingRestaurante);

        // Use domain service to update
        return restauranteDomainService.updateRestaurante(existingRestaurante);
    }

    private void updateRestauranteData(RestauranteRequest request, Restaurante existingRestaurante) {
        if (request.nome() != null && !request.nome().isBlank()) {
            existingRestaurante.setNome(request.nome());
        }
        if (request.tipoCozinha() != null && !request.tipoCozinha().isBlank()) {
            existingRestaurante.setTipoCozinha(request.tipoCozinha());
        }
        if (request.horarioFuncionamento() != null && !request.horarioFuncionamento().isBlank()) {
            existingRestaurante.setHorarioFuncionamento(request.horarioFuncionamento());
        }

        Usuario dono = usuarioDomainService.findUserById(request.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (dono) não encontrado com id: " + request.donoId()));
        existingRestaurante.setDono(dono);

        if (request.endereco() != null) {
            if (existingRestaurante.getEndereco() == null) {
                existingRestaurante.setEndereco(new Endereco());
            }
            existingRestaurante.getEndereco()
                    .updateAddress(request.endereco().logradouro(), request.endereco().numero(), request.endereco().complemento(), request.endereco().bairro(), request.endereco().cidade(), request.endereco().estado(), request.endereco().cep());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!restauranteDomainService.findRestauranteById(id).isPresent()) {
            throw new ResourceNotFoundException("Restaurante não encontrado com id: " + id);
        }
        restauranteDomainService.deleteRestaurante(id);
    }
}