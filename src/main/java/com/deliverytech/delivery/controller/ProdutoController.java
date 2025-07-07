package com.deliverytech.delivery.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Produto>> listarAtivos() {
        List<Produto> produtos = produtoService.listarAtivos();
        return ResponseEntity.ok(produtos);
    }
    
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Produto>> listarDisponiveis() {
        List<Produto> produtos = produtoService.listarDisponiveis();
        return ResponseEntity.ok(produtos);
    }
    
    @GetMapping("/promocoes")
    public ResponseEntity<List<Produto>> listarPromocoes() {
        List<Produto> produtos = produtoService.listarPromocoes();
        return ResponseEntity.ok(produtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> buscarPorCategoria(@PathVariable String categoria) {
        try {
            List<Produto> produtos = produtoService.buscarPorCategoria(categoria);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Produto>> buscarPorNome(@PathVariable String nome) {
        try {
            List<Produto> produtos = produtoService.buscarPorNome(nome);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        try {
            List<Produto> produtos = produtoService.buscarPorRestaurante(restauranteId);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<List<Produto>> buscarDisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.buscarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
    
    @GetMapping("/preco")
    public ResponseEntity<List<Produto>> buscarPorFaixaPreco(@RequestParam BigDecimal precoMin, @RequestParam BigDecimal precoMax) {
        try {
            List<Produto> produtos = produtoService.buscarPorFaixaPreco(precoMin, precoMax);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // preço menor ou igual a 20.00
    @GetMapping("/preco/{valor}")
    public ResponseEntity<List<Produto>> buscarPorPrecoMenorOuIgual(@PathVariable BigDecimal valor) {
        try { 
            List<Produto> produtos = produtoService.buscarPorPrecoMenorOuIgual(valor);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        List<String> categorias = produtoService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizar(id, produto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            produtoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/ativar-desativar")
    public ResponseEntity<Produto> ativarDesativarProduto(@PathVariable Long id) {
        try {
            Produto produtoAtualizado = produtoService.ativarDesativarProduto(id);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Produto> alterarDisponibilidade(@PathVariable Long id) {
        try {
            Produto produtoAtualizado = produtoService.alterarDisponibilidade(id);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/promocao/ativar")
    public ResponseEntity<Void> ativarPromocao(@PathVariable Long id, @RequestBody BigDecimal precoPromocional) {
        try {
            produtoService.ativarPromocao(id, precoPromocional);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/promocao/desativar")
    public ResponseEntity<Void> desativarPromocao(@PathVariable Long id) {
        try {
            produtoService.desativarPromocao(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
