-- Inserir clientes
INSERT INTO clientes (nome, email, telefone, endereco, data_criacao, ativo) VALUES
('João Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', NOW(), true),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - São Paulo/SP', NOW(), true),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - São Paulo/SP', NOW(), true);

-- Inserir restaurantes
INSERT INTO restaurantes (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, data_criacao, ativo) VALUES
('Pizzaria Bella', 'Italiana', 'Av. Paulista, 1000 - São Paulo/SP', '(11) 3333-1111', 5.00, 4.5, NOW(), true),
('Burger House', 'Hamburgueria', 'Rua Augusta, 500 - São Paulo/SP', '(11) 3333-2222', 3.50, 4.0, NOW(), true),
('Sushi Master', 'Japonesa', 'Rua Liberdade, 200 - São Paulo/SP', '(11) 3333-3333', 8.00, 4.8, NOW(), true);

-- Inserir produtos
INSERT INTO produto (nome, descricao, preco, categoria, ativo, disponivel, data_criacao, promocao, restaurante_id) VALUES

-- Pizzaria Bella
('Pizza Margherita', 'Molho de tomate, mussarela e manjericão', 35.90, 'Pizza', true, true, NOW(), false, 1),
('Pizza Calabresa', 'Molho de tomate, mussarela e calabresa', 38.90, 'Pizza', true, true, NOW(), false, 1),
('Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa', 28.90, 'Massa', true, true, NOW(), false, 1),

-- Burger House
('X-Burger', 'Hambúrguer, queijo, alface e tomate', 18.90, 'Hambúrguer', true, true, NOW(), false, 2),
('X-Bacon', 'Hambúrguer, queijo, bacon, alface e tomate', 22.90, 'Hambúrguer', true, true, NOW(), false, 2),
('Batata Frita', 'Porção de batata frita crocante', 12.90, 'Acompanhamento', true, true, NOW(), false, 2),

-- Sushi Master
('Combo Sashimi', '15 peças de sashimi variado', 45.90, 'Sashimi', true, true, NOW(), false, 3),
('Hot Roll Salmão', '8 peças de hot roll de salmão', 32.90, 'Hot Roll', true, true, NOW(), false, 3),
('Temaki Atum', 'Temaki de atum com cream cheese', 15.90, 'Temaki', true, true, NOW(), false, 3);

-- Inserir pedidos de exemplo
INSERT INTO pedidos (numero_pedido, data_pedido, endereco_entrega, status, valor_total, observacoes, data_criacao, cliente_id, restaurante_id) VALUES
('PED1234567890', NOW(), 'Rua A, 123 - São Paulo/SP', 'PENDENTE', 64.80, 'Sem cebola na pizza', NOW(), 1, 1),
('PED1234567891', NOW(), 'Rua B, 456 - São Paulo/SP', 'CONFIRMADO', 41.80, '', NOW(), 2, 2),
('PED1234567892', NOW(), 'Rua C, 789 - São Paulo/SP', 'ENTREGUE', 78.80, 'Wasabi à parte', NOW(), 3, 3);

-- Inserir itens dos pedidos
INSERT INTO itens_pedido (quantidade, preco_unitario, subtotal, pedido_id, produto_id) VALUES

-- Pedido 1 (João - Pizzaria Bella)
(1, 35.90, 35.90, 1, 1), -- Pizza Margherita
(1, 28.90, 28.90, 1, 3), -- Lasanha

-- Pedido 2 (Maria - Burger House)
(1, 22.90, 22.90, 2, 5), -- X-Bacon
(1, 18.90, 18.90, 2, 4), -- X-Burger

-- Pedido 3 (Pedro - Sushi Master)
(1, 45.90, 45.90, 3, 7), -- Combo Sashimi
(1, 32.90, 32.90, 3, 8); -- Hot Roll