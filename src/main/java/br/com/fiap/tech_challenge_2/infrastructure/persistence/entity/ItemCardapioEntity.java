package br.com.fiap.tech_challenge_2.infrastructure.persistence.entity;

import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCardapioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column
    private boolean disponivelApenasNoLocal;

    @Column
    private String fotoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnore
    private RestauranteEntity restaurante;

    // Convert to domain entity
    public ItemCardapio toDomain() {
        return new ItemCardapio(
            this.id,
            this.nome,
            this.descricao,
            this.preco,
            this.disponivelApenasNoLocal,
            this.fotoPath,
            this.restaurante != null ? this.restaurante.toDomain() : null
        );
    }

    // Conversão para domínio sem restaurante (evita ciclo)
    public ItemCardapio toDomainWithoutRestaurante() {
        return new ItemCardapio(
            this.id,
            this.nome,
            this.descricao,
            this.preco,
            this.disponivelApenasNoLocal,
            this.fotoPath,
            null
        );
    }

    // Convert from domain entity
    public static ItemCardapioEntity fromDomain(ItemCardapio itemCardapio) {
        ItemCardapioEntity entity = new ItemCardapioEntity();
        entity.setId(itemCardapio.getId());
        entity.setNome(itemCardapio.getNome());
        entity.setDescricao(itemCardapio.getDescricao());
        entity.setPreco(itemCardapio.getPreco());
        entity.setDisponivelApenasNoLocal(itemCardapio.isDisponivelApenasNoLocal());
        entity.setFotoPath(itemCardapio.getFotoPath());
        // Configurar apenas a referência do restaurante sem converter recursivamente
        if (itemCardapio.getRestaurante() != null) {
            RestauranteEntity restauranteRef = new RestauranteEntity();
            restauranteRef.setId(itemCardapio.getRestaurante().getId());
            entity.setRestaurante(restauranteRef);
        }
        return entity;
    }
} 