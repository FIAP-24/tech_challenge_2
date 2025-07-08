package br.com.fiap.tech_challenge_2.domain.model;

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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String email;

    private String perfil;

    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    private LocalDate dataUpdate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}