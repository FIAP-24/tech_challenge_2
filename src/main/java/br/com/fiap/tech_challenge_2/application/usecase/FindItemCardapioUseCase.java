package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;

import java.util.List;

public interface FindItemCardapioUseCase {
    
    /**
     * Finds all menu items in the system
     * 
     * @return List of all menu items
     */
    List<ItemCardapio> findAll();
    
    /**
     * Finds a menu item by ID
     * 
     * @param id Menu item ID
     * @return Menu item if found
     * @throws ResourceNotFoundException if menu item not found
     */
    ItemCardapio findById(Long id);
    
    /**
     * Finds all menu items for a specific restaurant
     * 
     * @param restauranteId Restaurant ID
     * @return List of menu items for the restaurant
     */
    List<ItemCardapio> findByRestauranteId(Long restauranteId);
    
    /**
     * Finds available menu items for a specific restaurant
     * 
     * @param restauranteId Restaurant ID
     * @return List of available menu items for the restaurant
     */
    List<ItemCardapio> findAvailableByRestauranteId(Long restauranteId);
} 