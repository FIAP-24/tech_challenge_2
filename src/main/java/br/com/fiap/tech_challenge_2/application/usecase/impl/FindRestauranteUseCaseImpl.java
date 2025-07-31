package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.usecase.FindRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindRestauranteUseCaseImpl implements FindRestauranteUseCase {

    private final RestauranteDomainService restauranteDomainService;
    private final RestauranteMapper restauranteMapper;

    @Override
    @Transactional(readOnly = true)
    public Set<RestauranteResponse> findAll() {
        return restauranteDomainService.findAllRestaurantes().stream()
                .map(restauranteMapper::toResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return restauranteDomainService.findRestauranteById(id)
                .map(restauranteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));
    }
} 