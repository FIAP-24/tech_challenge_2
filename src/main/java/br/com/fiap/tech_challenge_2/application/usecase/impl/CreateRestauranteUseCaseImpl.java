package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.CreateRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestauranteUseCaseImpl implements CreateRestauranteUseCase {

    private final UsuarioService usuarioService;
    private final RestauranteDomainService restauranteDomainService;
    private final RestauranteMapper restauranteMapper;

    @Override
    @Transactional
    public RestauranteResponse execute(RestauranteRequest request) {
        // Validate request
        validateRequest(request);

        // Check if name is available
        if (!restauranteDomainService.isRestauranteNameAvailable(request.nome())) {
            throw new DuplicateResourceException("Nome do restaurante já está em uso");
        }

        // Find owner user
        Usuario donoDomain = usuarioService.findById(request.donoId());

        // Convert to domain entity
        Restaurante restaurante = restauranteMapper.toEntity(request);
        restaurante.setDono(donoDomain);

        // Use domain service to create restaurant
        Restaurante saved = restauranteDomainService.createRestaurante(restaurante);

        return restauranteMapper.toResponse(saved);
    }

    private void validateRequest(RestauranteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante é obrigatório");
        }
        if (request.tipoCozinha() == null || request.tipoCozinha().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de cozinha é obrigatório");
        }
        if (request.horarioFuncionamento() == null || request.horarioFuncionamento().trim().isEmpty()) {
            throw new IllegalArgumentException("Horário de funcionamento é obrigatório");
        }
        if (request.donoId() == null) {
            throw new IllegalArgumentException("ID do dono é obrigatório");
        }
    }
} 