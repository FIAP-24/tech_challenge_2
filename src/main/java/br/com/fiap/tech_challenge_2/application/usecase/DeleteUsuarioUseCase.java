package br.com.fiap.tech_challenge_2.application.usecase;

public interface DeleteUsuarioUseCase {
    
    /**
     * Deletes a user from the system
     * 
     * @param id User ID to delete
     * @throws ResourceNotFoundException if user not found
     */
    void execute(Long id);
} 