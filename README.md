# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21.0.7 LTS**
- Spring Boot 3.5.3
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

## 📋 Endpoints
### Gerencial
#### Métodos GET
- /health - Status da aplicação (inclui versão Java)
- /info - Informações da aplicação
- /h2-console - Console do banco H2

---

### **Clientes**
#### Métodos GET
- /api/clientes - Lista todos os clientes
- /api/clientes/ativos - Lista todos os clientes ativos
- /api/clientes/{id} - Busca o cliente por id
- /api/clientes/email/{email} - Busca o cliente por e-mail
- /api/clientes/buscar - Busca o cliente por nome
- /api/clientes/verificar-email - Verifica se o e-mail já existe

#### Métodos POST
- /api/clientes - Cria um cliente

#### Métodos PUT
- /api/clientes/{id} - Atualiza um cliente pelo id

#### Métodos PATCH
- /api/clientes/{id}/ativar-desativar - Ativa ou desativa um cliente pelo id

#### Métodos DELETE
- /api/clientes/{id} - Deleta um cliente pelo id

---

### **Restaurantes**
#### Métodos GET
- /api/restaurantes - Lista todos os restaurantes
- /api/restaurantes/ativos - Lista todos os restaurantes ativos
- /api/restaurantes/{id} - Busca o restaurante por id
- /api/restaurantes/nome/{nome} - Busca o restaurante por nome exato
- /api/restaurantes/categoria/{categoria} - Busca restaurantes por categoria
- /api/restaurantes/buscar - Busca restaurantes por nome parcial
- /api/restaurantes/existe/{nome} - Verifica se o restaurante já existe pelo nome
- /api/restaurantes/preco/{precoMinimo}/{precoMaximo} - Busca restaurantes por faixa de preço
- /api/restaurantes/taxa-entrega - Busca restaurantes por taxa de entrega menor ou igual
- /api/restaurantes/top-cinco - Lista os 5 primeiros restaurantes por nome

#### Métodos POST
- /api/restaurantes - Cria um restaurante

#### Métodos PUT
- /api/restaurantes/{id} - Atualiza um restaurante pelo id

#### Métodos PATCH
- /api/restaurantes/{id}/ativar-desativar - Ativa ou desativa um restaurante pelo id

#### Métodos DELETE
- /api/restaurantes/{id} - Deleta um restaurante pelo id

---

#### **Pedidos**
#### Métodos GET
- /api/pedidos - Lista todos os pedidos
- /api/pedidos/{id} - Busca o pedido pelo id
- /api/pedidos/cliente/{clienteId} - Busca os pedidos pelo id do cliente
- /api/pedidos/restaurante/{restauranteId} - Busca os pedidos pelo id do restaurante
- /api/pedidos/status/{status} - Busca os pedidos pelos status
- /api/pedidos/ativos - Busca os pedidos ativos
- /api/pedidos/periodo" - Busca os pedidos por período

#### Métodos POST
- /api/pedidos - Cria um pedido

#### Métodos PUT
- /api/pedidos/{id} - Atualiza um pedido pelo id

#### Métodos PATCH
- /api/pedidos/{id}/confirmar - Confirma um pedido pelo id
- /api/pedidos/{id}/cancelar - Cancela um pedido pelo id
- /api/pedidos/{id}/entregar - Entrega um pedido pelo id
- /api/pedidos/{id}/status - Atualiza o status de um pedido pelo id
                                               s#### Métodos DELETE
- /api/pedidos/{id} - Deleta um pedido pelo id

---

#### **Produtos**
#### Métodos GET
- /api/produtos - Lista todos os produtos
- /api/produtos/ativos - Lista todos os produtos ativos
- /api/produtos/disponiveis - Lista todos os produtos disponíveis
- /api/produtos/promocoes - Lista todos os produtos em promoção
- /api/produtos/{id} - Busca o produto por id
- /api/produtos/categoria/{categoria} - Busca produtos por categoria
- /api/produtos/nome/{nome} - Busca uma lista de produtos por nome
- /api/produtos/restaurante/{restauranteId} - Busca produtos por restaurante
- /api/produtos/restaurante/{restauranteId}/disponiveis - Busca produtos disponíveis por restaurante
- /api/produtos/preco - Busca produtos por faixa de preço
- /api/produtos/preco/{valor} - Busca produtos por preço menor ou igual ao valor
- /api/produtos/categorias - Lista todas as categorias de produtos

#### Métodos POST
- /api/produtos - Cria um produto

#### Métodos PUT
- /api/produtos/{id} - Atualiza um produto pelo id

#### Métodos PATCH
- /api/produtos/{id}/ativar-desativar - Ativa ou desativa um produto pelo id
- /api/produtos/{id}/disponibilidade - Altera a disponibilidade de um produto pelo id
- /api/produtos/{id}/promocao/ativar - Ativa promoção de um produto pelo id
- /api/produtos/{id}/promocao/desativar - Desativa promoção de um produto pelo id

#### Métodos DELETE
- /api/produtos/{id} - Deleta um produto pelo id

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

## 👨‍💻 Desenvolvedor
- **George Wurthmann** - Turma 1
- Desenvolvido com JDK 21 e Spring Boot 3.5.3
- Banco de dados H2 (em memória)
- Spring Data JPA
- Spring Web
- Maven