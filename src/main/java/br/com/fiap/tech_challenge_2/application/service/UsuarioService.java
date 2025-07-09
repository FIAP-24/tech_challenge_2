package br.com.fiap.tech_challenge_2.application.service;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;

import java.util.Set;

public interface UsuarioService {

  UsuarioResponse save(Usuario usuario);

  Set<UsuarioResponse> findAll();

  UsuarioResponse findById(Long id);

  boolean authenticate(UsuarioLoginRequest loginRequest);

  void delete(Long id);

  UsuarioResponse update(Long id, UsuarioEditRequest request);
}
