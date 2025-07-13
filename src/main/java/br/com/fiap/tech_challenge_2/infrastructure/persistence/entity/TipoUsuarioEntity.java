package br.com.fiap.tech_challenge_2.infrastructure.persistence.entity;

import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    // Convert to domain entity
    public TipoUsuario toDomain() {
        return new TipoUsuario(this.id, this.nome);
    }

    // Convert from domain entity
    public static TipoUsuarioEntity fromDomain(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(tipoUsuario.getId());
        entity.setNome(tipoUsuario.getNome());
        return entity;
    }
} 