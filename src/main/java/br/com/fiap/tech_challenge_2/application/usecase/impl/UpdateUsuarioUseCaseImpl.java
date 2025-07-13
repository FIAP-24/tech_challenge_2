package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateUsuarioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateUsuarioUseCaseImpl implements UpdateUsuarioUseCase {

    private final UsuarioDomainService usuarioDomainService;
    private final UsuarioMapper usuarioMapper;
    private final PasswordHasher passwordHasher;

    @Override
    @Transactional
    public UsuarioResponse execute(Long id, UsuarioEditRequest request) {
        // Validate inputs
        validateInputs(id, request);
        
        // Find existing user
        Usuario existingUsuario = usuarioDomainService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

        // Update user data
        updateUsuarioData(request, existingUsuario);

        // Use domain service to update
        Usuario updated = usuarioDomainService.updateUser(existingUsuario);
        return usuarioMapper.toResponse(updated);
    }

    private void validateInputs(Long id, UsuarioEditRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
    }

    private void updateUsuarioData(UsuarioEditRequest request, Usuario existingUsuario) {
        // Update basic fields
        if (request.nome() != null && !request.nome().isBlank()) {
            existingUsuario.setNome(request.nome().trim());
        }
        if (request.email() != null && !request.email().isBlank()) {
            existingUsuario.setEmail(request.email().trim());
        }
        if (request.senha() != null && !request.senha().isEmpty()) {
            existingUsuario.updatePassword(passwordHasher.hashPassword(request.senha()));
        }
        
        // Update address if provided
        if (request.endereco() != null) {
            updateEndereco(request, existingUsuario);
        }
    }

    private void updateEndereco(UsuarioEditRequest request, Usuario existingUsuario) {
        // This would need to be implemented based on your Endereco model
        // For now, we'll leave it as a placeholder
        // You would need to create an EnderecoMapper and handle the address update
    }
} 