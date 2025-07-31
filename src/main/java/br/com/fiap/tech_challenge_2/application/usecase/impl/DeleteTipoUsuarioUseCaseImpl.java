package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.usecase.DeleteTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteTipoUsuarioUseCaseImpl implements DeleteTipoUsuarioUseCase {

    private final TipoUsuarioRepository repository;

    @Override
    @Transactional
    public void execute(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id);
        }
        
        repository.deleteById(id);
    }
} 