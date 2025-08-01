package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;

public interface CreateItemCardapioUseCase {
    
    /**
     * Creates a new menu item in the system
     * 
     * @param request Menu item creation request containing item data
     * @return Created menu item
     * @throws IllegalArgumentException if item data is invalid
     * @throws ResourceNotFoundException if restaurant not found
     */
    ItemCardapioResponse execute(ItemCardapioRequestDTO request);
} 