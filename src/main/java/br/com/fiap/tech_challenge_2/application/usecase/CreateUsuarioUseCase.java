package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;

public interface CreateUsuarioUseCase {
    
    /**
     * Creates a new user in the system
     * 
     * @param request User creation request containing user data
     * @return Created user response
     * @throws IllegalArgumentException if user data is invalid
     * @throws DuplicateResourceException if login already exists
     */
    UsuarioResponse execute(UsuarioRequest request);
} 