package br.com.fiap.tech_challenge_2.infrastructure.persistence.entity;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurante")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private EnderecoEntity endereco;

    @Column(nullable = false)
    private String tipoCozinha;

    @Column(nullable = false)
    private String horarioFuncionamento;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    @JsonIgnore
    private UsuarioEntity dono;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemCardapioEntity> cardapio;

    // Convert to domain entity
    public Restaurante toDomain() {
        return new Restaurante(
            this.id,
            this.nome,
            this.endereco != null ? this.endereco.toDomain() : null,
            this.tipoCozinha,
            this.horarioFuncionamento,
            this.dono != null ? this.dono.toDomain() : null,
            this.cardapio != null ? this.cardapio.stream()
                .map(item -> item.toDomainWithoutRestaurante())
                .collect(Collectors.toList()) : null
        );
    }

    // Convert from domain entity
    public static RestauranteEntity fromDomain(Restaurante restaurante) {
        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(restaurante.getId());
        entity.setNome(restaurante.getNome());
        entity.setTipoCozinha(restaurante.getTipoCozinha());
        entity.setHorarioFuncionamento(restaurante.getHorarioFuncionamento());
        
        if (restaurante.getEndereco() != null) {
            entity.setEndereco(EnderecoEntity.fromDomain(restaurante.getEndereco()));
        }
        
        if (restaurante.getDono() != null) {
            entity.setDono(UsuarioEntity.fromDomain(restaurante.getDono()));
        }
        
        // Não converter cardápio recursivamente para evitar ciclo
        // if (restaurante.getCardapio() != null) {
        //     entity.setCardapio(restaurante.getCardapio().stream()
        //         .map(ItemCardapioEntity::fromDomain)
        //         .collect(Collectors.toList()));
        // }
        
        return entity;
    }
} 