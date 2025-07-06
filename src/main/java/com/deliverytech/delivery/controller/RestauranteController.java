package com.deliverytech.delivery.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.service.RestauranteService;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {
    
    private final RestauranteService restauranteService;
    
    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }
    
    @GetMapping
    public ResponseEntity<List<Restaurante>> listarTodos() {
        List<Restaurante> restaurantes = restauranteService.listarTodos();
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Restaurante>> listarAtivos() {
        List<Restaurante> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorId(id);
        if (restaurante.isPresent()) {
            return ResponseEntity.ok(restaurante.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Restaurante> buscarPorNome(@PathVariable String nome) {
        Optional<Restaurante> restaurante = restauranteService.buscarPorNome(nome);
        if (restaurante.isPresent()) {
            return ResponseEntity.ok(restaurante.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Restaurante>> buscarPorCategoria(@PathVariable String categoria) {
        List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Restaurante>> buscarPorNomeParcial(@RequestParam String nome) {
        List<Restaurante> restaurantes = restauranteService.buscarPorNome(nome, true);
        return ResponseEntity.ok(restaurantes);
    }
    
    @PostMapping
    public ResponseEntity<Restaurante> cadastrar(@RequestBody Restaurante restaurante) {
        try {
            // Verificar se já existe um restaurante com o mesmo nome
            if (restauranteService.existePorNome(restaurante.getNome())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            Restaurante novoRestaurante = restauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoRestaurante);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        try {
            Restaurante restauranteAtualizado = restauranteService.atualizar(id, restaurante);
            if (restauranteAtualizado != null) {
                return ResponseEntity.ok(restauranteAtualizado);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (restauranteService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/existe/{nome}")
    public ResponseEntity<Boolean> existePorNome(@PathVariable String nome) {
        boolean existe = restauranteService.existePorNome(nome);
        return ResponseEntity.ok(existe);
    }

    @PatchMapping("/{id}/ativar-desativar")
    public ResponseEntity<Restaurante> ativarDesativarRestaurante(@PathVariable Long id) {
        Restaurante restauranteAtualizado = restauranteService.ativarDesativarRestaurante(id);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    @GetMapping("/preco/{precoMinimo}/{precoMaximo}")
    public ResponseEntity<List<Restaurante>> buscarPorPreco(@PathVariable BigDecimal precoMinimo, @PathVariable BigDecimal precoMaximo) {
        List<Restaurante> restaurantes = restauranteService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(restaurantes);
    }

    //listar taxa de entrega menor ou igual
    @GetMapping("/taxa-entrega")
    public ResponseEntity<List<Restaurante>> buscarPorTaxaEntrega(@RequestParam BigDecimal taxa) {
        List<Restaurante> restaurantes = restauranteService.buscarPorTaxaEntrega(taxa);
        return ResponseEntity.ok(restaurantes);
    }

    // Listar os 5 primeiros restaurantes por nome
    @GetMapping("/top-cinco")
    public ResponseEntity<List<Restaurante>> listarTop5PorNome() {
        List<Restaurante> top5Restaurantes = restauranteService.listarTop5PorNome();
        return ResponseEntity.ok(top5Restaurantes);
    }
}
