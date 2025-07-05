package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    // Buscar por nome
    Optional<Restaurante> findByNome(String nome);
    
    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();
    
    // Buscar por categoria
    List<Restaurante> findByCategoria(String categoria);
    
    // Buscar por nome contendo (case insensitive) e ativos
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    // Buscar por avaliação mínima
    List<Restaurante> findByAvaliacaoGreaterThanEqualAndAtivoTrue(BigDecimal avaliacao);

    // Ordenar por avaliação (descendente)
    List<Restaurante> findByAAtivoTrueOrderByAvaliacaoDesc();

    // Buscar por categoria e ativos
    List<Restaurante> findByCategoriaAndAtivoTrue(String categoria);
    
    // Verificar se restaurante já existe pelo nome
    boolean existsByNome(String nome);

    // Query customizada - restaurantes com produtos
    @Query("SELECT DISTINCT r FROM Restaurante r JOIN r.produtos p WHERE r.a􀆟vo = true")
    List<Restaurante> findRestaurantesComProdutos();

    // Buscar por faixa de taxa de entrega
    @Query("SELECT r FROM Restaurante r WHERE r.taxaEntrega BETWEEN :min AND :max AND r.ativo = true")
    List<Restaurante> findByTaxaEntregaBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    
    // Categorias disponíveis
    @Query("SELECT DISTINCT r.categoria FROM Restaurante r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDisponiveis();
}
