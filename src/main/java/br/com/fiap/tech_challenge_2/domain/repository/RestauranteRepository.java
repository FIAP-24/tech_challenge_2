package br.com.fiap.tech_challenge_2.domain.repository;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {
    
    Restaurante save(Restaurante restaurante);
    
    Optional<Restaurante> findById(Long id);
    
    List<Restaurante> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
    
    List<Restaurante> findByOwnerId(Long ownerId);
    
    Optional<Restaurante> findByNome(String nome);
} 