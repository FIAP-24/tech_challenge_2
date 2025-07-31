package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;

import java.util.List;

public interface FindItemCardapioUseCase {
    
    /**
     * Finds all menu items in the system
     * 
     * @return List of all menu items
     */
    List<ItemCardapioResponse> findAll();
    
    /**
     * Finds a menu item by ID
     * 
     * @param id Menu item ID
     * @return Menu item if found
     * @throws ResourceNotFoundException if menu item not found
     */
    ItemCardapioResponse findById(Long id);
    
    /**
     * Finds all menu items for a specific restaurant
     * 
     * @param restauranteId Restaurant ID
     * @return List of menu items for the restaurant
     */
    List<ItemCardapioResponse> findByRestauranteId(Long restauranteId);
    
    /**
     * Finds available menu items for a specific restaurant
     * 
     * @param restauranteId Restaurant ID
     * @return List of available menu items for the restaurant
     */
    List<ItemCardapioResponse> findAvailableByRestauranteId(Long restauranteId);
} 