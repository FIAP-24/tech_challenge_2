package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.ItemCardapioMapper;
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
    private final ItemCardapioMapper itemCardapioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapioResponse> findAll() {
        List<ItemCardapio> items = itemCardapioDomainService.findAllItemCardapios();
        return itemCardapioMapper.toResponseList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCardapioResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        ItemCardapio item = itemCardapioDomainService.findItemCardapioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com id: " + id));
        return itemCardapioMapper.toResponse(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapioResponse> findByRestauranteId(Long restauranteId) {
        if (restauranteId == null) {
            throw new IllegalArgumentException("Restaurante ID cannot be null");
        }
        
        List<ItemCardapio> items = itemCardapioDomainService.findItemCardapiosByRestauranteId(restauranteId);
        return itemCardapioMapper.toResponseList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCardapioResponse> findAvailableByRestauranteId(Long restauranteId) {
        if (restauranteId == null) {
            throw new IllegalArgumentException("Restaurante ID cannot be null");
        }
        
        List<ItemCardapio> items = itemCardapioDomainService.findAvailableItemCardapiosByRestauranteId(restauranteId);
        return itemCardapioMapper.toResponseList(items);
    }
} 