package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.usecase.CreateRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestauranteUseCaseImpl implements CreateRestauranteUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final RestauranteDomainService restauranteDomainService;
    private final EnderecoMapper enderecoMapper;

    @Override
    @Transactional
    public Restaurante execute(RestauranteRequestDTO request) {
        // Validate request
        validateRequest(request);
        
        // Find owner user
        Usuario owner = usuarioDomainService.findUserById(request.donoId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário dono não encontrado com id: " + request.donoId()));

        // Create restaurant
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(request.nome());
        restaurante.setTipoCozinha(request.tipoCozinha());
        restaurante.setHorarioFuncionamento(request.horarioFuncionamento());
        restaurante.setDono(owner);
        
        // Set address if provided
        if (request.endereco() != null) {
            restaurante.setEndereco(enderecoMapper.toEndereco(request.endereco()));
        }

        // Use domain service to create restaurant
        return restauranteDomainService.createRestaurante(restaurante);
    }

    private void validateRequest(RestauranteRequestDTO request) {
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