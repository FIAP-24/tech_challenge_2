package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.DeleteUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteUsuarioUseCaseImpl implements DeleteUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;

    @Override
    @Transactional
    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        // Check if user exists before deleting
        if (!usuarioDomainService.findUserById(id).isPresent()) {
            throw new ResourceNotFoundException("Usuário não encontrado com id: " + id);
        }
        
        usuarioDomainService.deleteUser(id);
    }
} 