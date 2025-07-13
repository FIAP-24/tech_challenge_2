package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.FindUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindUsuarioUseCaseImpl implements FindUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional(readOnly = true)
    public Set<UsuarioResponse> findAll() {
        return usuarioDomainService.findAllUsers().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return usuarioDomainService.findUserById(id)
                .map(usuarioMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }
} 