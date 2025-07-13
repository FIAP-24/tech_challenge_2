package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.usecase.AuthenticateUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticateUsuarioUseCaseImpl implements AuthenticateUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final PasswordHasher passwordHasher;

    @Override
    @Transactional(readOnly = true)
    public boolean execute(UsuarioLoginRequest request) {
        // Validate request
        validateRequest(request);
        
        // Find user by login
        Usuario usuario = usuarioDomainService.findUserByLogin(request.login())
                .orElseThrow(() -> new AuthenticationException("Login não encontrado"));

        // Verify password
        boolean authenticated = passwordHasher.verifyPassword(request.senha(), usuario.getSenha());
        if (!authenticated) {
            throw new AuthenticationException("Senha incorreta");
        }
        
        return true;
    }

    private void validateRequest(UsuarioLoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.login() == null || request.login().trim().isEmpty()) {
            throw new IllegalArgumentException("Login is required");
        }
        if (request.senha() == null || request.senha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha is required");
        }
    }
} 