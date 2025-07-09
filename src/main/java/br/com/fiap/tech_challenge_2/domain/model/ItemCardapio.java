package br.com.fiap.tech_challenge_2.domain.model;

public class ItemCardapio {

    private Long id;

    private String nome;

    private String descricao;

    private Double preco;

    private boolean disponivelApenasNoLocal;

    private String fotoPath; // Caminho para a foto

    private Restaurante restaurante;

    public ItemCardapio() {
    }

    public ItemCardapio(Long id, String nome, String descricao, Double preco, boolean disponivelApenasNoLocal, String fotoPath) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivelApenasNoLocal = disponivelApenasNoLocal;
        this.fotoPath = fotoPath;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public boolean isDisponivelApenasNoLocal() {
        return disponivelApenasNoLocal;
    }

    public void setDisponivelApenasNoLocal(boolean disponivelApenasNoLocal) {
        this.disponivelApenasNoLocal = disponivelApenasNoLocal;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}