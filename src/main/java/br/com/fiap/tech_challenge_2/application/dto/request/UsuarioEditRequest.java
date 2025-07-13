package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioEditRequest(

        String nome,

        @Email(message = "Email deve ser válido")
        String email,

        String perfil,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        EnderecoDTO endereco
) {}
