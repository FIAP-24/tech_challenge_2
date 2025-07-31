package br.com.fiap.tech_challenge_2.application.usecase;

public interface DeleteItemCardapioUseCase {

    /**
     * Deletes a item from the system
     *
     * @param id item ID to delete
     * @throws ResourceNotFoundException if item not found
     */
    void execute(Long id);
}
