package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteJpaRepository extends JpaRepository<RestauranteEntity, Long> {

    List<RestauranteEntity> findByDonoId(Long donoId);
    
    Optional<RestauranteEntity> findByNome(String nome);
} 