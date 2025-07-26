package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDomainService usuarioDomainService;
    private final UsuarioMapper usuarioMapper;
    private final PasswordHasher passwordHasher;

    @Override
    @Transactional
    public UsuarioResponse save(UsuarioRequest request) {
        // Check if login is available
        if (!usuarioDomainService.isLoginAvailable(request.login())) {
            throw new DuplicateResourceException("Login já está em uso");
        }

        // Convert to domain entity
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setSenha(passwordHasher.hashPassword(request.senha()));

        // Use domain service
        Usuario saved = usuarioDomainService.createUser(usuario);
        return usuarioMapper.toResponse(saved);
    }

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
        return usuarioDomainService.findUserById(id)
                .map(usuarioMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authenticate(UsuarioLoginRequest loginRequest) {
        Usuario usuario = usuarioDomainService.findUserByLogin(loginRequest.login())
                .orElseThrow(() -> new AuthenticationException("Login não encontrado"));

        boolean authenticated = passwordHasher.verifyPassword(loginRequest.senha(), usuario.getSenha());
        if (!authenticated) {
            throw new AuthenticationException("Senha incorreta");
        }
        return true;
    }

    @Override
    @Transactional
    public UsuarioResponse update(Long id, UsuarioEditRequest request) {
        Usuario existingUsuario = usuarioDomainService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

        // Update user data
        updateUsuarioData(request, existingUsuario);

        // Use domain service to update
        Usuario updated = usuarioDomainService.updateUser(existingUsuario);
        return usuarioMapper.toResponse(updated);
    }

    private void updateUsuarioData(UsuarioEditRequest request, Usuario existingUsuario) {
        if (request.nome() != null && !request.nome().isBlank()) {
            existingUsuario.setNome(request.nome());
        }
        if (request.email() != null && !request.email().isBlank()) {
            existingUsuario.setEmail(request.email());
        }
        if (request.senha() != null && !request.senha().isEmpty()) {
            existingUsuario.updatePassword(passwordHasher.hashPassword(request.senha()));
        }

        if (request.endereco() != null) {
            if (existingUsuario.getEndereco() == null) {
                existingUsuario.setEndereco(new Endereco());
            }
            existingUsuario.getEndereco()
                    .updateAddress(request.endereco().logradouro(), request.endereco().numero(), request.endereco().complemento(), request.endereco().bairro(), request.endereco().cidade(), request.endereco().estado(), request.endereco().cep());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!usuarioDomainService.findUserById(id).isPresent()) {
            throw new ResourceNotFoundException("Usuário não encontrado com id: " + id);
        }
        usuarioDomainService.deleteUser(id);
    }
}
