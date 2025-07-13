package br.com.fiap.tech_challenge_2.infrastructure.repository;

import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    Optional<TipoUsuario> findByNome(String perfil);
}
