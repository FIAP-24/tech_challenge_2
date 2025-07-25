package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.usecase.CreateItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CreateItemCardapioUseCaseImpl implements CreateItemCardapioUseCase {

    private final ItemCardapioDomainService itemCardapioDomainService;
    private final RestauranteDomainService restauranteDomainService;

    @Override
    @Transactional
    public ItemCardapio execute(ItemCardapioRequestDTO request) {
        // Validate request
        validateRequest(request);

        // Find restaurant
        Restaurante restaurante = restauranteDomainService.findRestauranteById(request.restauranteId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + request.restauranteId()));

        // Create menu item
        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setNome(request.nome());
        itemCardapio.setDescricao(request.descricao());
        itemCardapio.setPreco(request.preco());
        itemCardapio.setDisponivelApenasNoLocal(request.disponivelApenasNoLocal());
        itemCardapio.setFotoPath(request.fotoPath());
        itemCardapio.setRestaurante(restaurante);

        // Use domain service to create menu item
        return itemCardapioDomainService.createItemCardapio(itemCardapio);
    }

    private void validateRequest(ItemCardapioRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item é obrigatório");
        }
        if (request.preco() == null || request.preco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (request.restauranteId() == null) {
            throw new IllegalArgumentException("ID do restaurante é obrigatório");
        }
    }
} 