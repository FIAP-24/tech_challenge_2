package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.ItemCardapioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.repository.ItemCardapioRepository;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateItemCardapioUseCaseImpl implements UpdateItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;
    private final ItemCardapioMapper itemCardapioMapper;

    @Override
    @Transactional
    public ItemCardapioResponse execute(Long id, ItemCardapioRequestDTO request) {
        validateRequest(request);

        ItemCardapio existingItem = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com id: " + id));

        updateItemProperties(existingItem, request);

        ItemCardapio savedItem = itemCardapioRepository.save(existingItem);

        return itemCardapioMapper.toResponse(savedItem);
    }

    private void validateRequest(ItemCardapioRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome is required");
        }
        if (request.descricao() == null || request.descricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição is required");
        }
        if (request.preco() == null || request.preco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço must be greater than zero");
        }
        if (request.restauranteId() == null) {
            throw new IllegalArgumentException("Restaurante ID is required");
        }

        if (!restauranteRepository.existsById(request.restauranteId())) {
            throw new ResourceNotFoundException("Restaurante não encontrado com id: " + request.restauranteId());
        }
    }

    private void updateItemProperties(ItemCardapio item, ItemCardapioRequestDTO request) {
        item.setNome(request.nome());
        item.setDescricao(request.descricao());
        item.setPreco(request.preco());
        item.setDisponivelApenasNoLocal(request.disponivelApenasNoLocal());
        item.setFotoPath(request.fotoPath());

        if (!item.getRestaurante().getId().equals(request.restauranteId())) {
            restauranteRepository.findById(request.restauranteId())
                    .ifPresent(item::setRestaurante);
        }
    }
} 