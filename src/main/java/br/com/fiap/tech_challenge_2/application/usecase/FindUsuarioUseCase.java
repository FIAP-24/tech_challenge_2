package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;

import java.util.Set;

public interface FindUsuarioUseCase {
    
    /**
     * Finds all users in the system
     * 
     * @return Set of all users
     */
    Set<UsuarioResponse> findAll();
    
    /**
     * Finds a user by ID
     * 
     * @param id User ID
     * @return User response if found
     * @throws ResourceNotFoundException if user not found
     */
    UsuarioResponse findById(Long id);
} 