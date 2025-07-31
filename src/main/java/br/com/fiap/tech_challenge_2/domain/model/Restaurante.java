package br.com.fiap.tech_challenge_2.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurante {

    private Long id;
    private String nome;
    private Endereco endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private Usuario dono;
//    @JsonIgnore
    private List<ItemCardapio> cardapio;

    // Domain business logic methods
    public boolean isValidForRegistration() {
        return nome != null && !nome.trim().isEmpty() &&
               tipoCozinha != null && !tipoCozinha.trim().isEmpty() &&
               horarioFuncionamento != null && !horarioFuncionamento.trim().isEmpty() &&
               dono != null;
    }

    public void addMenuItem(ItemCardapio item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (cardapio == null) {
            cardapio = new java.util.ArrayList<>();
        }
        item.setRestaurante(this);
        cardapio.add(item);
    }

    public void removeMenuItem(Long itemId) {
        if (cardapio != null) {
            cardapio.removeIf(item -> item.getId().equals(itemId));
        }
    }

    public List<ItemCardapio> getAvailableMenuItems() {
        if (cardapio == null) {
            return new java.util.ArrayList<>();
        }
        return cardapio.stream()
                .filter(item -> !item.isDisponivelApenasNoLocal())
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean hasMenuItems() {
        return cardapio != null && !cardapio.isEmpty();
    }

    public void updateRestaurantInfo(String nome, String tipoCozinha, String horarioFuncionamento) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome.trim();
        }
        if (tipoCozinha != null && !tipoCozinha.trim().isEmpty()) {
            this.tipoCozinha = tipoCozinha.trim();
        }
        if (horarioFuncionamento != null && !horarioFuncionamento.trim().isEmpty()) {
            this.horarioFuncionamento = horarioFuncionamento.trim();
        }
    }
}