package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;

public interface UpdateUsuarioUseCase {
    
    /**
     * Updates an existing user
     * 
     * @param id User ID to update
     * @param request User update request containing new data
     * @return Updated user response
     * @throws ResourceNotFoundException if user not found
     * @throws IllegalArgumentException if update data is invalid
     */
    UsuarioResponse execute(Long id, UsuarioEditRequest request);
} 