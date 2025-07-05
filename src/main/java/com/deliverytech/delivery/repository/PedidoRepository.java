package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.enums.StatusPedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedidos por cliente
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

    // Buscar pedidos por cliente ID
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);
    
    // Buscar pedidos por restaurante
    List<Pedido> findByRestauranteOrderByDataPedidoDesc(Restaurante restaurante);
    
    // Buscar por status
    List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status);

    // Buscar por número do pedido
    Pedido findByNumeroPedido(String numeroPedido);

    // Buscar pedidos por período
    List<Pedido> findByDataPedidoBetweenOrderByDataPedidoDesc(LocalDateTime inicio, LocalDateTime fim);

    // Buscar pedidos do dia
    @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE ORDER BY p.dataPedido DESC")
    List<Pedido> findPedidosDodia();
    
    // Buscar pedidos por cliente e status
    List<Pedido> findByClienteAndStatus(Cliente cliente, StatusPedido status);
    
    // Buscar pedidos por restaurante e status
    List<Pedido> findByRestauranteAndStatus(Restaurante restaurante, StatusPedido status);
    
    // Buscar pedidos por data de pedido
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Consultas personalizadas - Buscar pedidos por restaurante id e ordenar por data do pedido
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteIdOrderByDataPedidoDesc(@Param("restauranteId") Long restauranteId);
    
    // Consultas personalizadas - Buscar pedidos por status e ordenar por data do pedido
    @Query("SELECT p FROM Pedido p WHERE p.status IN :statuses ORDER BY p.dataPedido ASC")
    List<Pedido> findByStatusInOrderByDataPedidoAsc(@Param("statuses") List<StatusPedido> statuses);
    
    // Consultas personalizadas - Buscar pedidos por cliente e status
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente = :cliente")
    Long countByCliente(@Param("cliente") Cliente cliente);
    
    // Consultas personalizadas - Contar pedidos por restaurante e status diferente de CANCELADO
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.restaurante = :restaurante AND p.status != 'CANCELADO'")
    Long countByRestauranteAndStatusNot(@Param("restaurante") Restaurante restaurante);

    // Relatório - pedidos por status
    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
    List<Object[]> countPedidosByStatus();
    
    // Pedidos pendentes (para dashboard)
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO') " +
            "ORDER BY p.dataPedido ASC")
    List<Pedido> findPedidosPendentes();
    
    // Valor total de vendas por período
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim " +
            "AND p.status NOT IN ('CANCELADO')")
    BigDecimal calcularVendasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
