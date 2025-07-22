package br.com.fiap.tech_challenge_2.application.service;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;

import java.util.List;

public interface TipoUsuarioService {
    TipoUsuarioDTO create(TipoUsuarioDTO dto);

    TipoUsuarioDTO findById(Long id);

    List<TipoUsuarioDTO> findAll();

    TipoUsuarioDTO update(Long id, TipoUsuarioDTO dto);

    void delete(Long id);
}