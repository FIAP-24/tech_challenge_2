package br.com.fiap.tech_challenge_2.domain.service;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioDomainService {
    
    ItemCardapio createItemCardapio(ItemCardapio itemCardapio);
    
    Optional<ItemCardapio> findItemCardapioById(Long id);
    
    List<ItemCardapio> findAllItemCardapios();
    
    ItemCardapio updateItemCardapio(ItemCardapio itemCardapio);
    
    void deleteItemCardapio(Long id);
    
    List<ItemCardapio> findItemCardapiosByRestauranteId(Long restauranteId);
    
    List<ItemCardapio> findAvailableItemCardapiosByRestauranteId(Long restauranteId);
} 