package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.repository.ItemCardapioRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.ItemCardapioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemCardapioRepositoryImpl implements ItemCardapioRepository {

    private final ItemCardapioJpaRepository jpaRepository;

    @Override
    public ItemCardapio save(ItemCardapio itemCardapio) {
        ItemCardapioEntity entity = ItemCardapioEntity.fromDomain(itemCardapio);
        ItemCardapioEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ItemCardapio> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ItemCardapioEntity::toDomain);
    }

    @Override
    public List<ItemCardapio> findAll() {
        return jpaRepository.findAll().stream()
                .map(ItemCardapioEntity::toDomain)
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
    public List<ItemCardapio> findByRestauranteId(Long restauranteId) {
        return jpaRepository.findByRestauranteId(restauranteId).stream()
                .map(ItemCardapioEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemCardapio> findAvailableByRestauranteId(Long restauranteId) {
        return jpaRepository.findByRestauranteIdAndDisponivelApenasNoLocalFalse(restauranteId).stream()
                .map(ItemCardapioEntity::toDomain)
                .collect(Collectors.toList());
    }
} 