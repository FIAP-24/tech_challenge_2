package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuarioEntity, Long> {
    
    Optional<TipoUsuarioEntity> findByNome(String nome);
} 