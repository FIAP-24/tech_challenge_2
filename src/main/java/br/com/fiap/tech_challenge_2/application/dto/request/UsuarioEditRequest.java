package br.com.fiap.tech_challenge_2.application.dto.request;

import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import jakarta.validation.constraints.*;

public record UsuarioEditRequest(

        String nome,

        @Email(message = "Email deve ser válido")
        String email,

        Perfil perfil,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        EnderecoDTO endereco
) {}
