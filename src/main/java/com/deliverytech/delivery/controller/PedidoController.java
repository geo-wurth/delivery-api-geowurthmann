package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.service.PedidoService;
import com.deliverytech.delivery.enums.StatusPedido;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok().body(pedido))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        try {
            Pedido novoPedido = pedidoService.salvar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido pedidoAtualizado = pedidoService.atualizar(id, pedido);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints específicos do negócio
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorClienteId(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Pedido>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<Pedido> pedidos = pedidoService.buscarPorRestauranteId(restauranteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> buscarPorStatus(@PathVariable String status) {
        try {
            StatusPedido statusEnum = StatusPedido.valueOf(status.toUpperCase());
            List<Pedido> pedidos = pedidoService.buscarPorStatus(statusEnum);
            return ResponseEntity.ok(pedidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Pedido>> buscarPedidosAtivos() {
        List<Pedido> pedidos = pedidoService.buscarPedidosAtivos();
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Pedido>> buscarPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dataInicio = LocalDateTime.parse(inicio, formatter);
            LocalDateTime dataFim = LocalDateTime.parse(fim, formatter);
            
            List<Pedido> pedidos = pedidoService.buscarPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Ações específicas
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Pedido> confirmarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id, @RequestBody(required = false) String motivo) {
        try {
            String motivoCancelamento = motivo != null ? motivo : "Cancelado pelo cliente";
            Pedido pedido = pedidoService.cancelarPedido(id, motivoCancelamento);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<Pedido> entregarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.entregarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(status.toUpperCase());
            Pedido pedido = pedidoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}