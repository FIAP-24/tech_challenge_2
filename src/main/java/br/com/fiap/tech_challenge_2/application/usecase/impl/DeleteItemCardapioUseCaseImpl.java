package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.DeleteItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteItemCardapioUseCaseImpl implements DeleteItemCardapioUseCase{

    private final ItemCardapioDomainService itemCardapioDomainService;

    @Override
    @Transactional
    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        boolean exists = itemCardapioDomainService.findItemCardapioById(id).isPresent();
        if (!exists) {
            throw new ResourceNotFoundException("Item não encontrado");
        }

        itemCardapioDomainService.deleteItemCardapio(id);
    }
}
