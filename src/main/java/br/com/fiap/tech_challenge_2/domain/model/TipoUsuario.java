package br.com.fiap.tech_challenge_2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuario {
    
    private Long id;
    private String nome;

    // Domain business logic methods
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty();
    }

    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome cannot be empty");
        }
        this.nome = newName.trim();
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(nome);
    }

    public boolean isCustomer() {
        return "CLIENTE".equalsIgnoreCase(nome);
    }

    public boolean isRestaurantOwner() {
        return "PROPRIETARIO".equalsIgnoreCase(nome);
    }
}