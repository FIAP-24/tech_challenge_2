package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;

public interface AuthenticateUsuarioUseCase {
    
    /**
     * Authenticates a user with login credentials
     * 
     * @param request Login request containing credentials
     * @return true if authentication successful, false otherwise
     * @throws AuthenticationException if login not found or password incorrect
     */
    boolean execute(UsuarioLoginRequest request);
} 