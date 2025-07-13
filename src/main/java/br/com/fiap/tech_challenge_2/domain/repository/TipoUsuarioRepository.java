package br.com.fiap.tech_challenge_2.domain.repository;

import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface TipoUsuarioRepository {
    
    TipoUsuario save(TipoUsuario tipoUsuario);
    
    Optional<TipoUsuario> findById(Long id);
    
    List<TipoUsuario> findAll();
    
    boolean existsById(Long id);
    
    void deleteById(Long id);
    
    Optional<TipoUsuario> findByNome(String nome);
} 