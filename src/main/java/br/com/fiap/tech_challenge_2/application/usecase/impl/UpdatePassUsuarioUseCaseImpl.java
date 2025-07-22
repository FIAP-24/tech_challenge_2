package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditPassRequest;
import br.com.fiap.tech_challenge_2.application.usecase.UpdatePassUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdatePassUsuarioUseCaseImpl implements UpdatePassUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final PasswordHasher passwordHasher;

    private void validateRequest(UsuarioEditPassRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.login() == null || request.login().trim().isEmpty()) {
            throw new IllegalArgumentException("Login is required");
        }
        if (!request.senhaNew().equals(request.senhaConfirm())) {
            throw new IllegalArgumentException("As senhas novas não são iguais");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean execute(UsuarioEditPassRequest passRequest) {
        // Validate request
        validateRequest(passRequest);

        // Find user by login
        Usuario usuario = usuarioDomainService.findUserByLogin(passRequest.login())
                .orElseThrow(() -> new AuthenticationException("Login não encontrado"));

        // Verify password
        boolean authenticated = passwordHasher.verifyPassword(passRequest.senhaOld(), usuario.getSenha());
        if (!authenticated) {
            throw new AuthenticationException("Senha incorreta");
        }
        usuario.setSenha(passwordHasher.hashPassword(passRequest.senhaNew()));
        usuarioDomainService.updateUser(usuario);
        return true;
    }
}