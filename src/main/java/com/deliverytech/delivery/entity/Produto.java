package com.deliverytech.delivery.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Column(nullable = false)
    private Boolean promocao = false;
    
    @Column(name = "preco_promocional", precision = 10, scale = 2)
    private BigDecimal precoPromocional;
    
    @Column(nullable = false, length = 100)
    private String categoria;
    
    @Column(name = "url_imagem")
    private String urlImagem;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(nullable = false)
    private Boolean disponivel = true;
    
    @Column(name = "tempo_preparo")
    private Integer tempoPreparo; // em minutos
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
    
    // Constructors
    public Produto() {
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.disponivel = true;
        this.promocao = false;
    }
    
    public Produto(String nome, String descricao, BigDecimal preco, String categoria, Restaurante restaurante) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.restaurante = restaurante;
    }
    
    // Getters and Setters
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
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public Boolean getPromocao() {
        return promocao;
    }
    
    public void setPromocao(Boolean promocao) {
        this.promocao = promocao;
    }
    
    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }
    
    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getUrlImagem() {
        return urlImagem;
    }
    
    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public Boolean getDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public Integer getTempoPreparo() {
        return tempoPreparo;
    }
    
    public void setTempoPreparo(Integer tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public Restaurante getRestaurante() {
        return restaurante;
    }
    
    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    // PrePersist e PreUpdate
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Métodos Utilitários
    public void inativar() {
        if (this.ativo != null && this.ativo) {
            this.ativo = false;
        }
    }

    public void ativar() {
        if (this.ativo != null && !this.ativo) {
            this.ativo = true;
        }
    }
    
    // Método utilitário para obter o preço efetivo (promocional se houver, senão preço normal)
    public BigDecimal getPrecoEfetivo() {
        return Boolean.TRUE.equals(promocao) && precoPromocional != null ? precoPromocional : preco;
    }
    
    // toString
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", promocao=" + promocao +
                ", precoPromocional=" + precoPromocional +
                ", categoria='" + categoria + '\'' +
                ", ativo=" + ativo +
                ", disponivel=" + disponivel +
                ", tempoPreparo=" + tempoPreparo +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
