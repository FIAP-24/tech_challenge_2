package br.com.fiap.tech_challenge_2.domain.service.impl;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.repository.UsuarioRepository;
import br.com.fiap.tech_challenge_2.domain.service.UsuarioDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioDomainServiceImpl implements UsuarioDomainService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario createUser(Usuario usuario) {
        // Domain validation
        if (!usuario.isValidForRegistration()) {
            throw new IllegalArgumentException("Dados do usuário inválidos para registro");
        }
        
        // Check if login is available
        if (!isLoginAvailable(usuario.getLogin())) {
            throw new IllegalArgumentException("Login já está em uso");
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findUserById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findUserByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    @Override
    public List<Usuario> findAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario updateUser(Usuario usuario) {
        // Validate if user exists
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUser(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean authenticateUser(String login, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);
        if (usuario.isEmpty()) {
            return false;
        }
        
        // In a real application, you would use a password hasher here
        // For now, we'll do a simple comparison
        return usuario.get().getSenha().equals(password);
    }

    @Override
    public boolean isLoginAvailable(String login) {
        return usuarioRepository.findByLogin(login).isEmpty();
    }
} 