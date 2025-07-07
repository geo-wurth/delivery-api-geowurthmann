package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Restaurante;

import com.deliverytech.delivery.enums.StatusPedido;

import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         RestauranteRepository restauranteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
    }
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    public Pedido salvar(Pedido pedido) {
        // 1. Validar cliente existe e está ativo
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente inativo não pode fazer pedidos"));

        if (cliente.getAtivo() == null || !cliente.getAtivo().booleanValue()) {
            throw new RuntimeException("Cliente inativo não pode fazer pedidos");
        }

        // 2. Validar restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(pedido.getRestaurante().getId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        if (restaurante.getAtivo() == null || !restaurante.getAtivo().booleanValue()) {
            throw new RuntimeException("Restaurante não está disponível");
        }

        // 3. Validar todos os produtos existem e estão disponíveis
        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + item.getProduto().getId()));

            if (produto.getAtivo() != null || !produto.getAtivo().booleanValue()) {
                throw new RuntimeException("Produto indisponível: " + produto.getNome());
            }

            if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
                throw new RuntimeException("Produto não pertence ao restaurante selecionado");
            }

            // Criar item do pedido
            ItemPedido itemNovo = new ItemPedido();
            itemNovo.setProduto(produto);
            itemNovo.setQuantidade(item.getQuantidade());
            itemNovo.setPrecoUnitario(produto.getPreco());
            itemNovo.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));

            itensPedido.add(itemNovo);
            subtotal = subtotal.add(itemNovo.getSubtotal());
        }

        // 4. Calcular total do pedido
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
        BigDecimal valorTotal = subtotal.add(taxaEntrega);

        // 5. Salvar pedido
        Pedido pedidoNovo = new Pedido();
        pedidoNovo.setNumeroPedido(pedido.getNumeroPedido());
        pedidoNovo.setObservacoes(pedido.getObservacoes());
        pedidoNovo.setCliente(cliente);
        pedidoNovo.setRestaurante(restaurante);
        pedidoNovo.setDataPedido(LocalDateTime.now());
        pedidoNovo.setStatus(StatusPedido.PENDENTE);
        pedidoNovo.setEnderecoEntrega(pedido.getEnderecoEntrega());
        pedidoNovo.setTaxaEntrega(taxaEntrega);
        pedidoNovo.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedidoNovo);

        // 6. Salvar itens do pedido
        for (ItemPedido item : itensPedido) {
            item.setPedido(pedidoSalvo);
        }
        pedidoSalvo.setItens(itensPedido);

        // 7. Atualizar estoque (se aplicável) - Simulação
        // Em um cenário real, aqui seria decrementado o estoque

        // 8. Retornar pedido criado
        return pedidoSalvo;
    }
    
    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (pedidoAtualizado.getStatus() != null) {
                        // Validar transição de status
                        if (!validaTransicao(pedido.getStatus(), pedidoAtualizado.getStatus())) {
                            throw new RuntimeException("Transição de status inválida de " + pedido.getStatus() + " para " + pedidoAtualizado.getStatus());
                        }
                        pedido.setStatus(pedidoAtualizado.getStatus());
                    }
                    if (pedidoAtualizado.getEnderecoEntrega() != null) {
                        pedido.setEnderecoEntrega(pedidoAtualizado.getEnderecoEntrega());
                    }
                    if (pedidoAtualizado.getObservacoes() != null) {
                        pedido.setObservacoes(pedidoAtualizado.getObservacoes());
                    }
                    if (pedidoAtualizado.getTempoEstimadoEntrega() != null) {
                        pedido.setTempoEstimadoEntrega(pedidoAtualizado.getTempoEstimadoEntrega());
                    }
                    if (pedidoAtualizado.getFormaPagamento() != null) {
                        pedido.setFormaPagamento(pedidoAtualizado.getFormaPagamento());
                    }
                    if (pedidoAtualizado.getTaxaEntrega() != null) {
                        pedido.setTaxaEntrega(pedidoAtualizado.getTaxaEntrega());
                    }
                    if (pedidoAtualizado.getValorDesconto() != null) {
                        pedido.setValorDesconto(pedidoAtualizado.getValorDesconto());
                    }
                    
                    pedido.calcularValorTotal();
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }
    
    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
    
    // Métodos específicos do negócio
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        List<Pedido> pedidosEncontrados = pedidoRepository.findByClienteOrderByDataPedidoDesc(cliente);
        if (pedidosEncontrados == null || pedidosEncontrados.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o cliente: " + cliente.getId());
        }
        return pedidosEncontrados;
    }
    
    public List<Pedido> buscarPorRestaurante(Restaurante restaurante) {
        List<Pedido> pedidosEncontrados = pedidoRepository.findByRestauranteOrderByDataPedidoDesc(restaurante);
        if (pedidosEncontrados == null || pedidosEncontrados.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o restaurante: " + restaurante.getId());
        }
        return pedidosEncontrados;
    }
    
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        List<Pedido> pedidosEncontrados = pedidoRepository.findByStatusOrderByDataPedidoDesc(status);
        if (pedidosEncontrados == null || pedidosEncontrados.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o status: " + status);
        }
        return pedidosEncontrados;
    }
    
    public List<Pedido> buscarPorClienteId(Long clienteId) {
        List<Pedido> pedidosEncontrados = pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);
        if (pedidosEncontrados == null || pedidosEncontrados.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o cliente: " + clienteId);
        }
        return pedidosEncontrados;
    }
    
    public List<Pedido> buscarPorRestauranteId(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdOrderByDataPedidoDesc(restauranteId);
    }
    
    public List<Pedido> buscarPedidosAtivos() {
        List<StatusPedido> statusAtivos = Arrays.asList(
            StatusPedido.PENDENTE,
            StatusPedido.CONFIRMADO,
            StatusPedido.PREPARANDO,
            StatusPedido.PRONTO,
            StatusPedido.SAIU_PARA_ENTREGA
        );
        return pedidoRepository.findByStatusInOrderByDataPedidoAsc(statusAtivos);
    }
    
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }
    
    public Pedido confirmarPedido(Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // Verificar se o pedido já está confirmado
                    if (pedido.getStatus() == StatusPedido.CONFIRMADO) {
                        throw new RuntimeException("Pedido já está confirmado: " + id);
                    }

                    // Validar transição de status
                    if (!validaTransicao(pedido.getStatus(), StatusPedido.CONFIRMADO)) {
                        throw new RuntimeException("Transição de status inválida de " + pedido.getStatus() + " para " + StatusPedido.CONFIRMADO);
                    }
                    // Atualizar status do pedido para CONFIRMADO
                    pedido.setStatus(StatusPedido.CONFIRMADO);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }
    
    public Pedido cancelarPedido(Long id, String motivo) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

        // Verificar se o pedido já está cancelado
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido já está cancelado: " + id);
        }

        // Verificar se o pedido pode ser cancelado
        if (!podeSerCancelado(pedido.getStatus())) {
            throw new RuntimeException("Pedido não pode ser cancelado no status: " + pedido.getStatus());
        }

        // Validar transição de status
        if (!validaTransicao(pedido.getStatus(), StatusPedido.CANCELADO)) {
            throw new RuntimeException("Transição de status inválida de " + pedido.getStatus() + " para " + StatusPedido.CANCELADO);
        }

        // Atualizar status do pedido para CANCELADO
        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setMotivoCancelamento(motivo);
        pedido.setDataCancelamento(LocalDateTime.now());
        pedido.setTempoEstimadoEntrega(null); // Limpar tempo estimado de entrega

        // Salva e retornar pedido atualizado
        return pedidoRepository.save(pedido);
    }
    
    public Pedido entregarPedido(Long id) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.entregar();
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }
    
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // Verificar se o pedido já está no status desejado
                    if (pedido.getStatus() == novoStatus) {
                        throw new RuntimeException("Pedido já está no status: " + novoStatus);
                    }

                    // Validar transição de status
                    if (!validaTransicao(pedido.getStatus(), novoStatus)) {
                        throw new RuntimeException("Transição de status inválida de " + pedido.getStatus() + " para " + novoStatus);
                    }

                    // Verificar se o novo status é CANCELADO
                    if (novoStatus == StatusPedido.CANCELADO && !podeSerCancelado(pedido.getStatus())) {
                        throw new RuntimeException("Pedido não pode ser cancelado no status: " + pedido.getStatus());
                    }

                    // Atualizar status do pedido
                    pedido.setStatus(novoStatus);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }
    
    public Long contarPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.countByCliente(cliente);
    }
    
    public Long contarPedidosAtivosPorRestaurante(Restaurante restaurante) {
        return pedidoRepository.countByRestauranteAndStatusNot(restaurante);
    }

    public BigDecimal calcularValorTotalPedido(List<ItemPedido> itens) {
        // Calcular o valor total do pedido somando os preços dos itens
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + item.getProduto().getId()));
            valorTotal = valorTotal.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }
        return valorTotal;
    }

    private boolean validaTransicao(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Implementar lógica de transições válidas
        switch (statusAtual) {
            case PENDENTE:
                return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;
            case CONFIRMADO:
                return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;
            case PREPARANDO:
                return novoStatus == StatusPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA:
                return novoStatus == StatusPedido.ENTREGUE;
            default:
                return false;
        }
    }

    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }
}