package br.com.fiap.tech_challenge_2.domain.service;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteDomainService {
    
    Restaurante createRestaurante(Restaurante restaurante);
    
    Optional<Restaurante> findRestauranteById(Long id);
    
    List<Restaurante> findAllRestaurantes();
    
    Restaurante updateRestaurante(Restaurante restaurante);
    
    void deleteRestaurante(Long id);
    
    List<Restaurante> findRestaurantesByOwner(Long ownerId);
    
    boolean isRestauranteNameAvailable(String nome);
} 