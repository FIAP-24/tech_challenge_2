package br.com.fiap.tech_challenge_2.infrastructure.repository;

import br.com.fiap.tech_challenge_2.infrastructure.Entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<RestauranteEntity, Long> {
}