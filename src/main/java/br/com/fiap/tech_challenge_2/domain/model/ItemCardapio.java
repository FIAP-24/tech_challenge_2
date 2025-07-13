package br.com.fiap.tech_challenge_2.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCardapio {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private boolean disponivelApenasNoLocal;
    private String fotoPath;
    @JsonIgnore
    private Restaurante restaurante;

    // Domain business logic methods
    public boolean isValidForRegistration() {
        return nome != null && !nome.trim().isEmpty() &&
               preco != null && preco > 0;
    }

    public boolean isAvailable() {
        return !disponivelApenasNoLocal;
    }

    public void updatePrice(Double newPrice) {
        if (newPrice == null || newPrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.preco = newPrice;
    }

    public void updateAvailability(boolean disponivelApenasNoLocal) {
        this.disponivelApenasNoLocal = disponivelApenasNoLocal;
    }

    public void updateItemInfo(String nome, String descricao, Double preco) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome.trim();
        }
        if (descricao != null) {
            this.descricao = descricao.trim();
        }
        if (preco != null && preco > 0) {
            this.preco = preco;
        }
    }

    public String getFormattedPrice() {
        if (preco == null) {
            return "Preço não informado";
        }
        return String.format("R$ %.2f", preco);
    }
}