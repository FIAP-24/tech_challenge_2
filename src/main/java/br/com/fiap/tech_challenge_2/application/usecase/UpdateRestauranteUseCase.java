package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;

public interface UpdateRestauranteUseCase {
    /**
     * Updates an existing restaurant
     *
     * @param id Restaurant ID to update
     * @param request Restaurant update request containing new data
     * @return Updated restaurant response
     * @throws ResourceNotFoundException if restaurant not found
     * @throws IllegalArgumentException if update data is invalid
     */
    RestauranteResponse execute(Long id, RestauranteRequest request);
}