package br.com.fiap.tech_challenge_2.domain.repository;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioRepository {
    
    ItemCardapio save(ItemCardapio itemCardapio);
    
    Optional<ItemCardapio> findById(Long id);
    
    List<ItemCardapio> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
    
    List<ItemCardapio> findByRestauranteId(Long restauranteId);
    
    List<ItemCardapio> findAvailableByRestauranteId(Long restauranteId);
} 