package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Cliente salvar(Cliente cliente) {
        if (cliente.getId() == null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            
            // Verificar se o email não está sendo usado por outro cliente
            if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) && 
                clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
                throw new RuntimeException("Email já está em uso");
            }
            
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            cliente.setEndereco(clienteAtualizado.getEndereco());
            cliente.setAtivo(clienteAtualizado.getAtivo());
            
            return clienteRepository.save(cliente);
        }
        throw new RuntimeException("Cliente não encontrado");
    }
    
    public void deletar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }
    
    public Cliente ativarDesativarCliente(Long id) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente c = clienteExistente.get();
            c.setAtivo(!c.getAtivo());
            clienteRepository.save(c);
            return c;
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
}
