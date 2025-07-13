package br.com.fiap.tech_challenge_2.domain.service.impl;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.domain.service.RestauranteDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestauranteDomainServiceImpl implements RestauranteDomainService {

    private final RestauranteRepository restauranteRepository;

    @Override
    public Restaurante createRestaurante(Restaurante restaurante) {
        // Domain validation
        if (!restaurante.isValidForRegistration()) {
            throw new IllegalArgumentException("Dados do restaurante inválidos para registro");
        }
        
        // Check if restaurant name is available
        if (!isRestauranteNameAvailable(restaurante.getNome())) {
            throw new IllegalArgumentException("Nome do restaurante já está em uso");
        }
        
        return restauranteRepository.save(restaurante);
    }

    @Override
    public Optional<Restaurante> findRestauranteById(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    public List<Restaurante> findAllRestaurantes() {
        return restauranteRepository.findAll();
    }

    @Override
    public Restaurante updateRestaurante(Restaurante restaurante) {
        // Validate if restaurant exists
        if (!restauranteRepository.existsById(restaurante.getId())) {
            throw new IllegalArgumentException("Restaurante não encontrado");
        }
        
        return restauranteRepository.save(restaurante);
    }

    @Override
    public void deleteRestaurante(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurante não encontrado");
        }
        restauranteRepository.deleteById(id);
    }

    @Override
    public List<Restaurante> findRestaurantesByOwner(Long ownerId) {
        return restauranteRepository.findByOwnerId(ownerId);
    }

    @Override
    public boolean isRestauranteNameAvailable(String nome) {
        return restauranteRepository.findByNome(nome).isEmpty();
    }
} 