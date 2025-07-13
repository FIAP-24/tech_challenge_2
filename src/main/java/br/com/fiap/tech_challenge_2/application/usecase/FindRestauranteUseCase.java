package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;

import java.util.List;

public interface FindRestauranteUseCase {

    /**
     * Finds all restaurants in the system
     *
     * @return List of all restaurants
     */
    List<Restaurante> findAll();

    /**
     * Finds a restaurant by ID
     *
     * @param id Restaurant ID
     * @return Restaurant if found
     * @throws ResourceNotFoundException if restaurant not found
     */
    Restaurante findById(Long id);
} 