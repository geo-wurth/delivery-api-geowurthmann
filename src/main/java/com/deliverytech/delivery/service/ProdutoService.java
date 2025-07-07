package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.ProdutoRepository;

@Service
@Transactional
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }
    
    public List<Produto> listarAtivos() {
        return produtoRepository.findByAtivoTrue();
    }
    
    public List<Produto> listarDisponiveis() {
        return produtoRepository.findByAtivoTrueAndDisponivelTrue();
    }
    
    public List<Produto> listarPromocoes() {
        return produtoRepository.findByPromocaoTrueAndDisponivelTrue();
    }
    
    public List<Produto> buscarPorCategoria(String categoria) {
        List<Produto> produtosEncontrados = produtoRepository.findByCategoriaAndDisponivelTrue(categoria);
        if (produtosEncontrados.isEmpty()) {
            throw new RuntimeException("Nenhum produto encontrado para a categoria: " + categoria);
        }
        return produtosEncontrados;
    }
    
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtosEncontrados = produtoRepository.findByNomeContainingIgnoreCaseAndDisponivelTrue(nome);
        
        if (produtosEncontrados.isEmpty()) {
                throw new RuntimeException("Produto não encontrado: " + nome);
        }

        return produtosEncontrados;
    }
    
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        List<Produto> produtosEncontrados = produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);

        if (produtosEncontrados.isEmpty()) {
            throw new RuntimeException("Nenhum produto encontrado para o restaurante ID: " + restauranteId);
        }

        return produtosEncontrados;
    }
    
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        List<Produto> produtosEncontrados = produtoRepository.findByPrecoBetweenAndDisponivelTrue(precoMin, precoMax);
        if (produtosEncontrados.isEmpty()) {
            throw new RuntimeException("Nenhum produto encontrado na faixa de preço: " + precoMin + " - " + precoMax);
        }
        return produtosEncontrados;
    }

    public List<Produto> buscarPorPrecoMenorOuIgual(BigDecimal valor) {
        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqualAndDisponivelTrue(valor);
        if (produtos.isEmpty()) {
            throw new RuntimeException("Nenhum produto encontrado com preço menor ou igual a: " + valor);
        }
        return produtos;
    }
    
    public List<Produto> buscarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findProdutosDisponiveisPorRestaurante(restauranteId);
    }
    
    public List<String> listarCategorias() {
        return produtoRepository.findAllCategorias();
    }
    
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
    
    public Produto salvar(Produto produto) {

        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id)
            .map(produto -> {
                produto.setNome(produtoAtualizado.getNome());
                produto.setDescricao(produtoAtualizado.getDescricao());
                produto.setPreco(produtoAtualizado.getPreco());
                produto.setPromocao(produtoAtualizado.getPromocao());
                produto.setPrecoPromocional(produtoAtualizado.getPrecoPromocional());
                produto.setCategoria(produtoAtualizado.getCategoria());
                produto.setUrlImagem(produtoAtualizado.getUrlImagem());
                produto.setDisponivel(produtoAtualizado.getDisponivel());
                produto.setTempoPreparo(produtoAtualizado.getTempoPreparo());
                produto.setRestaurante(produtoAtualizado.getRestaurante());
                return produtoRepository.save(produto);
            })
            .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
    }
    
    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
    
    public Produto ativarDesativarProduto(Long id) {
        Optional<Produto> produtoEncontrado = produtoRepository.findById(id);
        if (produtoEncontrado.isEmpty()) {
            throw new RuntimeException("Produto não encontrado com id: " + id);
        }
        produtoEncontrado.get().setAtivo(!produtoEncontrado.get().getAtivo());
        return produtoRepository.save(produtoEncontrado.get());
    }
    
    public Produto alterarDisponibilidade(Long id) {
        Optional<Produto> produtoEncontrado = produtoRepository.findById(id);
        if (produtoEncontrado.isEmpty()) {
            throw new RuntimeException("Produto não encontrado com id: " + id);
        }
        produtoEncontrado.get().setDisponivel(!produtoEncontrado.get().getDisponivel());
        return produtoRepository.save(produtoEncontrado.get());
    }
    
    public void ativarPromocao(Long id, BigDecimal precoPromocional) {
        produtoRepository.findById(id)
            .ifPresentOrElse(
                produto -> {
                    produto.setPromocao(true);
                    produto.setPrecoPromocional(precoPromocional);
                    produtoRepository.save(produto);
                },
                () -> { throw new RuntimeException("Produto não encontrado com id: " + id); }
            );
    }
    
    public void desativarPromocao(Long id) {
        produtoRepository.findById(id)
            .ifPresentOrElse(
                produto -> {
                    produto.setPromocao(false);
                    produto.setPrecoPromocional(null);
                    produtoRepository.save(produto);
                },
                () -> { throw new RuntimeException("Produto não encontrado com id: " + id); }
            );
    }
}
