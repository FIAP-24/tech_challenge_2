package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioEntity, Long> {

    List<ItemCardapioEntity> findByRestauranteId(Long restauranteId);
    
    List<ItemCardapioEntity> findByRestauranteIdAndDisponivelApenasNoLocalFalse(Long restauranteId);
} 