package br.com.fiap.tech_challenge_2.domain.model;

import java.util.List;


public class Restaurante {

    private Long id;

    private String nome;

    private Endereco endereco;

    private String tipoCozinha;

    private String horarioFuncionamento; // Ex: "Seg-Sex: 09:00-22:00, Sab: 10:00-23:00"

    private Usuario dono;

    private List<ItemCardapio> cardapio;

    public Restaurante() {
    }
    public Restaurante(Long id, String nome, Endereco endereco, String tipoCozinha, String horarioFuncionamento) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public List<ItemCardapio> getCardapio() {
        return cardapio;
    }

    public void setCardapio(List<ItemCardapio> cardapio) {
        this.cardapio = cardapio;
    }
}