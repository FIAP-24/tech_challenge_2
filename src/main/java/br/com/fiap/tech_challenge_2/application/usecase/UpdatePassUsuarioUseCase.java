package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditPassRequest;

public interface UpdatePassUsuarioUseCase {

    boolean execute(UsuarioEditPassRequest passRequest);
}
