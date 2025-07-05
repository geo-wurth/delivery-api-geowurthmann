package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Buscar produtos por restaurante e disponíveis
    List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);

    // Buscar produtos por restaurante ID e disponíveis
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    // Buscar produtos por categoria e disponíveis
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);

    // Buscar produtos por nome contendo (case insensitive) e disponíveis
    List<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);

    // Buscar produtos por faixa de preço e disponíveis
    List<Produto> findByPrecoBetweenAndDisponivelTrue(BigDecimal precoMin, BigDecimal precoMax);

    // Buscar produtos mais baratos que um valor e disponíveis
    List<Produto> findByPrecoLessThanEqualAndDisponivelTrue(BigDecimal preco);

    // Ordenar produtos por preço
    List<Produto> findByDisponivelTrueOrderByPrecoAsc();
    List<Produto> findByDisponivelTrueOrderByPrecoDesc();

    // Buscar produtos ativos e disponíveis
    List<Produto> findByAtivoTrue();
    List<Produto> findByDisponivelTrue();
    List<Produto> findByAtivoTrueAndDisponivelTrue();

    // Buscar produtos em promoção e disponíveis
    List<Produto> findByPromocaoTrueAndDisponivelTrue();

    // Buscar produtos por restaurante ID, ativos e disponíveis
    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.disponivel = true AND p.restaurante.id = :restauranteId")
    List<Produto> findProdutosDisponiveisPorRestaurante(@Param("restauranteId") Long restauranteId);

    // Buscar produtos em todas as categorias
    @Query("SELECT DISTINCT p.categoria FROM Produto p WHERE p.ativo = true")
    List<String> findAllCategorias();

    // Buscar produtos por preço efetivo (considerando promoção) menor ou igual a um valor
    // Considera o preço promocional se estiver ativo, caso contrário, considera o preço normal
    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.disponivel = true AND " +
           "(p.promocao = false OR (p.promocao = true AND p.precoPromocional <= :precoMax)) AND " +
           "(p.promocao = true OR (p.promocao = false AND p.preco <= :precoMax))")
    List<Produto> findByPrecoEfetivoLessThanEqual(@Param("precoMax") BigDecimal precoMax);

    // Query customizada - Buscar produtos mais vendidos
    @Query("SELECT p FROM Produto p JOIN p.itensPedido ip " +
            "GROUP BY p ORDER BY COUNT(ip) DESC")
    List<Produto> findProdutosMaisVendidos();

    // Buscar produtos por restaurante e categoria
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId " +
            "AND p.categoria = :categoria AND p.disponivel = true")
    List<Produto> findByRestauranteAndCategoria(@Param("restauranteId") Long restauranteId,
                                                @Param("categoria") String categoria);

    // Contar produtos por restaurante
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    Long countByRestauranteId(@Param("restauranteId") Long restauranteId);
}
