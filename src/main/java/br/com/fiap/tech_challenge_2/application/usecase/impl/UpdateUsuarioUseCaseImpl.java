package br.com.fiap.tech_challenge_2.application.usecase.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.TipoUsuarioMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.service.TipoUsuarioService;
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
    private final EnderecoMapper enderecoMapper;
    private final PasswordHasher passwordHasher;
    private final TipoUsuarioService tipoUsuarioService;
    private final TipoUsuarioMapper tipoUsuarioMapper;

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

        // Update tipo usuario if provided
        if (request.tipoUsuarioId() != null) {

            var tipoUsuarioDTO = tipoUsuarioService.findById(request.tipoUsuarioId());
            var tipoUsuarioDomain = tipoUsuarioMapper.toEntity(tipoUsuarioDTO);
            existingUsuario.setTipoUsuario(tipoUsuarioDomain);

        }
    }

    private void updateEndereco(UsuarioEditRequest request, Usuario existingUsuario) {
        // Convert DTO to domain model
        var enderecoDTO = request.endereco();
        var enderecoDomain = enderecoMapper.toEndereco(enderecoDTO);

        // Get existing address or create new one
        var existingEndereco = existingUsuario.getEndereco();
        if (existingEndereco == null) {
            // If no existing address, set the new one
            existingUsuario.setEndereco(enderecoDomain);
        } else {
            // If existing address, update it with new values
            existingEndereco.updateAddress(
                    enderecoDomain.getLogradouro(),
                    enderecoDomain.getNumero(),
                    enderecoDomain.getComplemento(),
                    enderecoDomain.getBairro(),
                    enderecoDomain.getCidade(),
                    enderecoDomain.getEstado(),
                    enderecoDomain.getCep()
            );
        }
    }
} 