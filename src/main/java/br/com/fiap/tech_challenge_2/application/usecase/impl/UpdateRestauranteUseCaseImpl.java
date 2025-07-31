package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.RestauranteMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateRestauranteUseCaseImpl implements UpdateRestauranteUseCase {
    private final RestauranteDomainService restauranteDomainService;
    private final RestauranteMapper restauranteMapper;
    private final EnderecoMapper enderecoMapper;
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public RestauranteResponse execute(Long id, RestauranteRequest request) {
        // Validate inputs
        validateInputs(id, request);

        // Find existing restaurant
        Restaurante existingRestaurante = restauranteDomainService.findRestauranteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com id: " + id));

        // Update restaurant data
        updateRestauranteData(request, existingRestaurante);

        // Use domain service to update
        Restaurante updated = restauranteDomainService.updateRestaurante(existingRestaurante);
        return restauranteMapper.toResponse(updated);
    }

    private void validateInputs(Long id, RestauranteRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
    }

    private void updateRestauranteData(RestauranteRequest request, Restaurante existingRestaurante) {
        // Update basic fields
        if (request.nome() != null && !request.nome().isBlank()) {
            existingRestaurante.setNome(request.nome().trim());
        }
        if (request.tipoCozinha() != null && !request.tipoCozinha().isBlank()) {
            existingRestaurante.setTipoCozinha(request.tipoCozinha().trim());
        }
        if (request.horarioFuncionamento() != null && !request.horarioFuncionamento().isBlank()) {
            existingRestaurante.setHorarioFuncionamento(request.horarioFuncionamento().trim());
        }

        // Update address if provided
        if (request.endereco() != null) {
            updateEndereco(request, existingRestaurante);
        }

        // Update dono Id if provided
        if (request.donoId() != null) {
            Usuario donoDomain = usuarioService.findById(request.donoId());
            existingRestaurante.setDono(donoDomain);
        }
    }

    private void updateEndereco(RestauranteRequest request, Restaurante existingRestaurante) {
        // Convert DTO to domain model
        var enderecoDTO = request.endereco();
        var enderecoDomain = enderecoMapper.toEndereco(enderecoDTO);

        // Get existing address or create new one
        var existingEndereco = existingRestaurante.getEndereco();
        if (existingEndereco == null) {
            // If no existing address, set the new one
            existingRestaurante.setEndereco(enderecoDomain);
        } else {
            // If existing address, update it with new values
            existingEndereco.updateAddress(
                    enderecoDomain.getLogradouro(),
                    enderecoDomain.getNumero(),
                    enderecoDomain.getComplemento(),
                    enderecoDomain.getBairro(),
                    enderecoDomain.getCidade(),
                    enderecoDomain.getEstado(),
                    enderecoDomain.getCep()
            );
        }
    }
}
