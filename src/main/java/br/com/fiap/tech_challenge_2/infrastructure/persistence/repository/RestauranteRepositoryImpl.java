package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.repository.RestauranteRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.RestauranteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RestauranteRepositoryImpl implements RestauranteRepository {

    private final RestauranteJpaRepository jpaRepository;

    @Override
    public Restaurante save(Restaurante restaurante) {
        RestauranteEntity entity = RestauranteEntity.fromDomain(restaurante);
        RestauranteEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRepository.findById(id)
                .map(RestauranteEntity::toDomain);
    }

    @Override
    public List<Restaurante> findAll() {
        return jpaRepository.findAll().stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Restaurante> findByOwnerId(Long ownerId) {
        return jpaRepository.findByDonoId(ownerId).stream()
                .map(RestauranteEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Restaurante> findByNome(String nome) {
        return jpaRepository.findByNome(nome)
                .map(RestauranteEntity::toDomain);
    }
} 