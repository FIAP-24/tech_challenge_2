package br.com.fiap.tech_challenge_2.domain.service;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioDomainService {
    
    Usuario createUser(Usuario usuario);
    
    Optional<Usuario> findUserById(Long id);
    
    Optional<Usuario> findUserByLogin(String login);
    
    List<Usuario> findAllUsers();
    
    Usuario updateUser(Usuario usuario);
    
    void deleteUser(Long id);
    
    boolean authenticateUser(String login, String password);
    
    boolean isLoginAvailable(String login);
} 