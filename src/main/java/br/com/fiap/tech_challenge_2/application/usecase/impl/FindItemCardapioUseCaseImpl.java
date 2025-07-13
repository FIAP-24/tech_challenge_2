package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.FindItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindItemCardapioUseCaseImpl implements FindItemCardapioUseCase {

    private final ItemCardapioDomainService itemCardapioDomainService;

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> findAll() {
        return itemCardapioDomainService.findAllItemCardapios();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCardapio findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return itemCardapioDomainService.findItemCardapioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> findByRestauranteId(Long restauranteId) {
        if (restauranteId == null) {
            throw new IllegalArgumentException("Restaurante ID cannot be null");
        }
        
        return itemCardapioDomainService.findItemCardapiosByRestauranteId(restauranteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapio> findAvailableByRestauranteId(Long restauranteId) {
        if (restauranteId == null) {
            throw new IllegalArgumentException("Restaurante ID cannot be null");
        }
        
        return itemCardapioDomainService.findAvailableItemCardapiosByRestauranteId(restauranteId);
    }
} 