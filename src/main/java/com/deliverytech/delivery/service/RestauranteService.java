package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    
    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }
    
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }
    
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }
    
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }
    
    public Optional<Restaurante> buscarPorNome(String nome) {
        return restauranteRepository.findByNome(nome);
    }
    
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }
    
    public List<Restaurante> buscarPorNome(String nome, boolean ignorarCase) {
        if (ignorarCase) {
            return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        }
        return restauranteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }
    
    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }
    
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Optional<Restaurante> restauranteExistente = restauranteRepository.findById(id);
        if (restauranteExistente.isPresent()) {
            Restaurante restaurante = restauranteExistente.get();
            restaurante.setNome(restauranteAtualizado.getNome());
            restaurante.setCategoria(restauranteAtualizado.getCategoria());
            restaurante.setAtivo(restauranteAtualizado.getAtivo());
            return restauranteRepository.save(restaurante);
        }
        return null;
    }
    
    public boolean deletar(Long id) {
        if (restauranteRepository.existsById(id)) {
            restauranteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Restaurante ativarDesativarRestaurante(Long id) {
        Optional<Restaurante> restauranteExistente = restauranteRepository.findById(id);
        if (restauranteExistente.isPresent()) {
            Restaurante r = restauranteExistente.get();
            r.setAtivo(!r.getAtivo());
            restauranteRepository.save(r);
            return r;
        } else {
            throw new RuntimeException("Restaurante não encontrado");
        }
    }
    
    public boolean existePorNome(String nome) {
        return restauranteRepository.existsByNome(nome);
    }

    public List<Restaurante> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        // Buscar restaurantes por taxa de entrega dentro do intervalo
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaBetween(precoMinimo, precoMaximo);
        if (restaurantes.isEmpty()) {
            throw new RuntimeException("Nenhum restaurante encontrado com taxa de entrega entre " + precoMinimo + " e " + precoMaximo);
        }
        return restaurantes;
    }

    public List<Restaurante> buscarPorTaxaEntrega(BigDecimal taxaEntrega) {
        // Buscar restaurantes por taxa de entrega
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(taxaEntrega);
        if (restaurantes.isEmpty()) {
            throw new RuntimeException("Nenhum restaurante encontrado com taxa de entrega menor ou igual a: " + taxaEntrega);
        }
        return restaurantes;
    }

    public List<Restaurante> listarTop5PorNome() {
        // Buscar os 5 primeiros restaurantes por nome
        List<Restaurante> top5Restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        if (top5Restaurantes.isEmpty()) {
            throw new RuntimeException("Nenhum restaurante encontrado.");
        }
        return top5Restaurantes;
    }
}
