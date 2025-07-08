package br.com.fiap.tech_challenge_2.application.dto.request;

import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Email(message = "Email deve ser válido")
        String email,

        @NotNull(message = "Perfil é obrigatório")
        Perfil perfil,

        @NotBlank(message = "Login é obrigatório")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        EnderecoDTO endereco
) {
}
