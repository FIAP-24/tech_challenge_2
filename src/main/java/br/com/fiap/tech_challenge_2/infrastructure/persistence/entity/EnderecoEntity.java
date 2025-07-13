package br.com.fiap.tech_challenge_2.infrastructure.persistence.entity;

import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false)
    private String numero;

    @Column
    private String complemento;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cep;

    // Convert to domain entity
    public Endereco toDomain() {
        return new Endereco(
            this.id,
            this.logradouro,
            this.numero,
            this.complemento,
            this.bairro,
            this.cidade,
            this.estado,
            this.cep
        );
    }

    // Convert from domain entity
    public static EnderecoEntity fromDomain(Endereco endereco) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(endereco.getId());
        entity.setLogradouro(endereco.getLogradouro());
        entity.setNumero(endereco.getNumero());
        entity.setComplemento(endereco.getComplemento());
        entity.setBairro(endereco.getBairro());
        entity.setCidade(endereco.getCidade());
        entity.setEstado(endereco.getEstado());
        entity.setCep(endereco.getCep());
        return entity;
    }
} 