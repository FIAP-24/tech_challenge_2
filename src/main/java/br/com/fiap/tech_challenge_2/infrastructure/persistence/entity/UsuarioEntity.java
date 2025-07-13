package br.com.fiap.tech_challenge_2.infrastructure.persistence.entity;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    private LocalDate dataUpdate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_usuario_id")
    private TipoUsuarioEntity tipoUsuario;

    // Convert to domain entity
    public Usuario toDomain() {
        return new Usuario(
            this.id,
            this.nome,
            this.email,
            this.login,
            this.senha,
            this.dataUpdate,
            this.endereco != null ? this.endereco.toDomain() : null,
            this.tipoUsuario != null ? this.tipoUsuario.toDomain() : null
        );
    }

    // Convert from domain entity
    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setLogin(usuario.getLogin());
        entity.setSenha(usuario.getSenha());
        entity.setDataUpdate(usuario.getDataUpdate());
        
        if (usuario.getEndereco() != null) {
            entity.setEndereco(EnderecoEntity.fromDomain(usuario.getEndereco()));
        }
        
        if (usuario.getTipoUsuario() != null) {
            entity.setTipoUsuario(TipoUsuarioEntity.fromDomain(usuario.getTipoUsuario()));
        }
        
        return entity;
    }
} 