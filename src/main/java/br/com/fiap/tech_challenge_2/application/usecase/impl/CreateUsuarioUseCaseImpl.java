package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.CreateUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateUsuarioUseCaseImpl implements CreateUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final UsuarioMapper usuarioMapper;
    private final PasswordHasher passwordHasher;

    @Override
    @Transactional
    public UsuarioResponse execute(UsuarioRequest request) {
        // Validate request
        validateRequest(request);
        
        // Check if login is available
        if (!usuarioDomainService.isLoginAvailable(request.login())) {
            throw new DuplicateResourceException("Login já está em uso");
        }

        // Convert to domain entity
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setSenha(passwordHasher.hashPassword(request.senha()));

        // Use domain service to create user
        Usuario saved = usuarioDomainService.createUser(usuario);
        
        return usuarioMapper.toResponse(saved);
    }

    private void validateRequest(UsuarioRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.nome() == null || request.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome is required");
        }
        if (request.login() == null || request.login().trim().isEmpty()) {
            throw new IllegalArgumentException("Login is required");
        }
        if (request.senha() == null || request.senha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha is required");
        }
        if (request.senha().length() < 6) {
            throw new IllegalArgumentException("Senha must be at least 6 characters");
        }
    }
} 