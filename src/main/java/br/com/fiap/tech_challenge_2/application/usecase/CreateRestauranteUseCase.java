package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;

public interface CreateRestauranteUseCase {
    
    /**
     * Creates a new restaurant in the system
     * 
     * @param request Restaurant creation request containing restaurant data
     * @return Created restaurant
     * @throws IllegalArgumentException if restaurant data is invalid
     * @throws ResourceNotFoundException if owner user not found
     */
    Restaurante execute(RestauranteRequestDTO request);
} 