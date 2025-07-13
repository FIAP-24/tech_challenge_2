package br.com.fiap.tech_challenge_2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private LocalDate dataUpdate;
    private Endereco endereco;
    private TipoUsuario tipoUsuario;

    // Domain business logic methods
    public boolean isValidForRegistration() {
        return nome != null && !nome.trim().isEmpty() &&
               login != null && !login.trim().isEmpty() &&
               senha != null && !senha.trim().isEmpty();
    }

    public void updatePassword(String newPassword) {
        this.senha = newPassword;
        this.dataUpdate = LocalDate.now();
    }

    public void updateProfile(String nome, String email) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        this.dataUpdate = LocalDate.now();
    }
}