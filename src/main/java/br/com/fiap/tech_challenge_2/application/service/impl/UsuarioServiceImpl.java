package br.com.fiap.tech_challenge_2.application.service.impl;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.infrastructure.repository.UsuarioRepository;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import br.com.fiap.tech_challenge_2.infrastructure.utils.PasswordHasher;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
  @RequiredArgsConstructor
  public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordHasher passwordHasher;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoMapper enderecoMapper;


  @Override
  @Transactional
  public UsuarioResponse save(UsuarioRequest request) {

      usuarioRepository
          .findByLogin(request.login())
          .ifPresent(
              u -> {
                throw new DuplicateResourceException("Login já está em uso");
              });

      Usuario usuario = usuarioMapper.toEntity(request);
      usuario.setSenha(passwordHasher.hashPassword(request.senha()));

      Usuario saved = usuarioRepository.save(usuario);
      return usuarioMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UsuarioResponse> findAll() {
      return usuarioRepository.findAll().stream()
          .map(usuarioMapper::toResponse)
          .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
      return usuarioRepository
          .findById(id)
          .map(usuarioMapper::toResponse)
          .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authenticate(UsuarioLoginRequest loginRequest) {
      Usuario usuario =
          usuarioRepository
              .findByLogin(loginRequest.login())
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
      Usuario existingUsuario =
          usuarioRepository
              .findById(id)
              .orElseThrow(
                  () -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

      if (request.endereco() != null) {
        updateEndereco(request, existingUsuario);
      }

      updateUsuario(request, existingUsuario);

      Usuario updated = usuarioRepository.save(existingUsuario);
      return usuarioMapper.toResponse(updated);
    }

      private void updateUsuario(UsuarioEditRequest request, Usuario existingUsuario) {
          existingUsuario.setNome((request.nome() != null && !request.nome().isBlank()) ? request.nome() : existingUsuario.getNome());
          existingUsuario.setEmail((request.email() != null && !request.email().isBlank()) ? request.email() : existingUsuario.getEmail());
          existingUsuario.setPerfil((request.perfil() != null && !request.perfil().name().isBlank()) ? request.perfil().name() : existingUsuario.getPerfil());
          existingUsuario.setDataUpdate(LocalDate.now());

          if (request.senha() != null && !request.senha().isEmpty()) {
              existingUsuario.setSenha(passwordHasher.hashPassword(request.senha()));
          }
      }


    private void updateEndereco(UsuarioEditRequest request, Usuario existingUsuario) {
      if (existingUsuario.getEndereco() == null) {
        existingUsuario.setEndereco(enderecoMapper.toEndereco(request.endereco()));
      }else {
        existingUsuario.getEndereco().setBairro((request.endereco().bairro() != null && !request.endereco().bairro().isBlank() ? request.endereco().bairro() : existingUsuario.getEndereco().getBairro()));
        existingUsuario.getEndereco().setCidade((request.endereco().cidade() != null && !request.endereco().cidade().isBlank() ? request.endereco().cidade() : existingUsuario.getEndereco().getCidade()));
        existingUsuario.getEndereco().setLogradouro((request.endereco().logradouro() != null && !request.endereco().logradouro().isBlank() ? request.endereco().logradouro() : existingUsuario.getEndereco().getLogradouro()));
        existingUsuario.getEndereco().setCep((request.endereco().cep() != null && !request.endereco().cep().isBlank() ? request.endereco().cep() : existingUsuario.getEndereco().getCep()));
        existingUsuario.getEndereco().setComplemento((request.endereco().complemento() != null && !request.endereco().complemento().isBlank() ? request.endereco().complemento() : existingUsuario.getEndereco().getComplemento()));
        existingUsuario.getEndereco().setEstado((request.endereco().estado() != null && !request.endereco().estado().isBlank() ? request.endereco().estado() : existingUsuario.getEndereco().getEstado()));
        existingUsuario.getEndereco().setNumero((request.endereco().numero() != null && !request.endereco().numero().isBlank() ? request.endereco().numero() : existingUsuario.getEndereco().getNumero()));
      }
    }

    @Override
    @Transactional
    public void delete(Long id) {
      if (!usuarioRepository.existsById(id)) {
        throw new ResourceNotFoundException("Usuário não encontrado com id: " + id);
      }
      usuarioRepository.deleteById(id);
    }
  }
