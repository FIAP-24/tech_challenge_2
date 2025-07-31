package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;

public interface CreateTipoUsuarioUseCase {

    TipoUsuarioDTO execute(TipoUsuarioDTO request);
} 