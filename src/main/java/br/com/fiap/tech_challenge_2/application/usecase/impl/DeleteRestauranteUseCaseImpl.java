package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.DeleteRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteRestauranteUseCaseImpl implements DeleteRestauranteUseCase {
    private final RestauranteDomainService restauranteDomainService;

    @Override
    @Transactional
    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        // Check if user exists before deleting
        if (!restauranteDomainService.findRestauranteById(id).isPresent()) {
            throw new ResourceNotFoundException("Restaurante não encontrado com id: " + id);
        }

        restauranteDomainService.deleteRestaurante(id);
    }
}
