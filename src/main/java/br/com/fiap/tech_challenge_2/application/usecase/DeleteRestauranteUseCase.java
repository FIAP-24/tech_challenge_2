package br.com.fiap.tech_challenge_2.application.usecase;

public interface DeleteRestauranteUseCase {

    /**
     * Deletes a restaurant from the system
     *
     * @param id Restaurant ID to delete
     * @throws ResourceNotFoundException if restaurant not found
     */
    void execute(Long id);
}