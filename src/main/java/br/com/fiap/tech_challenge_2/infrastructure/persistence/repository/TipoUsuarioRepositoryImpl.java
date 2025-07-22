package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.domain.exception.ForeignKeyViolationException;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.TipoUsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TipoUsuarioRepositoryImpl implements TipoUsuarioRepository {

    private final TipoUsuarioJpaRepository jpaRepository;

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = TipoUsuarioEntity.fromDomain(tipoUsuario);
        TipoUsuarioEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<TipoUsuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(TipoUsuarioEntity::toDomain);
    }

    @Override
    public List<TipoUsuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(TipoUsuarioEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        try {
            jpaRepository.deleteById(id);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ForeignKeyViolationException(
                        "Não é possível excluir este tipo de usuário pois existem usuários vinculados a ele."
                );
            }
            throw e;
        }
    }

    @Override
    public Optional<TipoUsuario> findByNome(String nome) {
        return jpaRepository.findByNome(nome)
                .map(TipoUsuarioEntity::toDomain);
    }
} 