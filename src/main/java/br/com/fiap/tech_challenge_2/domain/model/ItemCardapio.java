package br.com.fiap.tech_challenge_2.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCardapio {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean disponivelApenasNoLocal;
    private String fotoPath;

    private Restaurante restaurante;

    // Domain business logic methods
    public boolean isValidForRegistration() {
        return nome != null && !nome.trim().isEmpty() &&
                preco != null && preco.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isAvailable() {
        return !disponivelApenasNoLocal;
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.preco = newPrice;
    }

    public void updateAvailability(boolean disponivelApenasNoLocal) {
        this.disponivelApenasNoLocal = disponivelApenasNoLocal;
    }

    public void updateItemInfo(String nome, String descricao, BigDecimal preco) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome.trim();
        }
        if (descricao != null) {
            this.descricao = descricao.trim();
        }
        if (preco != null && preco.compareTo(BigDecimal.ZERO) > 0) {
            this.preco = preco;
        }
    }

    public String getFormattedPrice() {
        if (preco == null) {
            return "Preço não informado";
        }

        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(preco).replace("\u00A0", " ");
    }
}