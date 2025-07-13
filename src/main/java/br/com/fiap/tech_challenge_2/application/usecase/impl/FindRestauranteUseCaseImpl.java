package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.FindRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindRestauranteUseCaseImpl implements FindRestauranteUseCase {

    private final RestauranteDomainService restauranteDomainService;

    @Override
    @Transactional(readOnly = true)
    public List<Restaurante> findAll() {
        return restauranteDomainService.findAllRestaurantes();
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurante findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return restauranteDomainService.findRestauranteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));
    }
} 