package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;

import java.util.List;

public interface FindTipoUsuarioUseCase {

    List<TipoUsuarioDTO> findAll();

    TipoUsuarioDTO findById(Long id);
} 