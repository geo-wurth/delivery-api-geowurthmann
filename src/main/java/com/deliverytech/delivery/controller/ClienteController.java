package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.service.ClienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;
    
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Cliente>> listarAtivos() {
        List<Cliente> clientes = clienteService.listarAtivos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNome(@Param("nome") String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }
    
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/ativar-desativar")
    public ResponseEntity<Cliente> ativarDesativarCliente(@PathVariable Long id) {
        try {
            Cliente clienteAtualizado = clienteService.ativarDesativarCliente(id);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/verificar-email")
    public ResponseEntity<Boolean> verificarEmail(@RequestParam String email) {
        boolean existe = clienteService.existeEmail(email);
        return ResponseEntity.ok(existe);
    }
}
