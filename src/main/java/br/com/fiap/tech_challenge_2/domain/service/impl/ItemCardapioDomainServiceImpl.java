package br.com.fiap.tech_challenge_2.domain.service.impl;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.repository.ItemCardapioRepository;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemCardapioDomainServiceImpl implements ItemCardapioDomainService {

    private final ItemCardapioRepository itemCardapioRepository;

    @Override
    public ItemCardapio createItemCardapio(ItemCardapio itemCardapio) {
        // Domain validation
        if (!itemCardapio.isValidForRegistration()) {
            throw new IllegalArgumentException("Dados do item do cardápio inválidos para registro");
        }
        
        return itemCardapioRepository.save(itemCardapio);
    }

    @Override
    public Optional<ItemCardapio> findItemCardapioById(Long id) {
        return itemCardapioRepository.findById(id);
    }

    @Override
    public List<ItemCardapio> findAllItemCardapios() {
        return itemCardapioRepository.findAll();
    }

    @Override
    public ItemCardapio updateItemCardapio(ItemCardapio itemCardapio) {
        // Validate if item exists
        if (!itemCardapioRepository.existsById(itemCardapio.getId())) {
            throw new IllegalArgumentException("Item do cardápio não encontrado");
        }
        
        return itemCardapioRepository.save(itemCardapio);
    }

    @Override
    public void deleteItemCardapio(Long id) {
        if (!itemCardapioRepository.existsById(id)) {
            throw new IllegalArgumentException("Item do cardápio não encontrado");
        }
        itemCardapioRepository.deleteById(id);
    }

    @Override
    public List<ItemCardapio> findItemCardapiosByRestauranteId(Long restauranteId) {
        return itemCardapioRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public List<ItemCardapio> findAvailableItemCardapiosByRestauranteId(Long restauranteId) {
        return itemCardapioRepository.findAvailableByRestauranteId(restauranteId);
    }
} 