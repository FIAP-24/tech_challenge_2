package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.repository.TipoUsuarioRepository;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateTipoUsuarioUseCaseImpl implements UpdateTipoUsuarioUseCase {

    private final TipoUsuarioRepository repository;
    private final TipoUsuarioMapper mapper;

    @Override
    @Transactional
    public TipoUsuarioDTO execute(Long id, TipoUsuarioDTO request) {
        validateRequest(request);

        TipoUsuario tipoUsuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado com id: " + id));

        tipoUsuario.setNome(request.nome());
        TipoUsuario saved = repository.save(tipoUsuario);

        return mapper.toDTO(saved);
    }

    private void validateRequest(TipoUsuarioDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome is required");
        }
    }
} 