package br.com.fiap.tech_challenge_2.domain.repository;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    
    Usuario save(Usuario usuario);
    
    Optional<Usuario> findById(Long id);
    
    Optional<Usuario> findByLogin(String login);
    
    List<Usuario> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
} 