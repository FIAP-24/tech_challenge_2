# Tech Challenge 2 - Sistema de Gerenciamento de Restaurantes

Uma API REST Spring Boot para gerenciamento de restaurantes construída com princípios de Clean Architecture, incluindo gerenciamento de usuários, operações de restaurantes e gerenciamento de cardápios.

## 🏗️ Arquitetura

Este projeto segue os princípios da **Clean Architecture** com separação clara de responsabilidades:

```
src/main/java/br/com/fiap/tech_challenge_2/
├── domain/                    # Camada de Domínio (Lógica de Negócio Central)
│   ├── enums/                # Enums do Domínio
│   ├── model/                # Entidades do Domínio (Objetos de Negócio Puros)
│   ├── repository/           # Interfaces dos Repositórios
│   ├── service/              # Serviços do Domínio
│   └── exception/            # Exceções do Domínio
├── application/              # Camada de Aplicação (Casos de Uso)
│   ├── dto/                  # Objetos de Transferência de Dados
│   ├── mapper/               # Mapeadores de Objetos
│   ├── service/              # Serviços de Aplicação
│   └── usecase/              # Interfaces e Implementações dos Casos de Uso
├── infrastructure/           # Camada de Infraestrutura (Preocupações Externas)
│   ├── config/               # Classes de Configuração
│   ├── persistence/          # Implementação do Banco de Dados
│   │   ├── entity/           # Entidades JPA
│   │   └── repository/       # Implementações dos Repositórios
│   └── utils/                # Utilitários de Infraestrutura
└── interfaces/               # Camada de Interface (Controladores)
    ├── controller/           # Controladores REST
    └── exception/            # Tratadores de Exceção
```

## 🚀 Funcionalidades

### Funcionalidades Principais
- **Gerenciamento de Usuários**: Criar, ler, atualizar, deletar e autenticar usuários
- **Gerenciamento de Restaurantes**: Gerenciar informações e operações de restaurantes
- **Gerenciamento de Cardápios**: Manipular itens de cardápio e categorias
- **Gerenciamento de Endereços**: Tratamento abrangente de endereços
- **Tipos de Usuário**: Suporte para diferentes papéis de usuário (CLIENTE, PROPRIETARIO)

### Funcionalidades Técnicas
- **Clean Architecture**: Separação rigorosa de responsabilidades
- **Domain-Driven Design**: Modelos de domínio ricos com lógica de negócio
- **Repository Pattern**: Camada abstrata de acesso a dados
- **Use Case Pattern**: Camada de aplicação orquestra operações de negócio
- **Value Objects**: Email e Login como objetos de valor do domínio
- **Domain Events**: Arquitetura orientada a eventos para eventos de negócio
- **Testes Abrangentes**: Testes unitários para casos de uso e lógica de domínio
- **Documentação da API**: Integração com Swagger/OpenAPI
- **Tratamento de Exceções**: Gerenciamento centralizado de exceções
- **Segurança de Senhas**: Hash seguro de senhas

## 🛠️ Stack Tecnológica

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**
- **MapStruct** (Mapeamento de Objetos)
- **Lombok** (Redução de Código Boilerplate)
- **JUnit 5** (Testes)
- **Mockito** (Mocking)
- **Swagger/OpenAPI** (Documentação da API)

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional, para implantação containerizada)

## 🔧 Instalação e Configuração

### 1. Clonar o Repositório
```bash
git clone <url-do-repositório>
cd tech_challenge_2
```

### 2. Configuração do Banco de Dados
Crie um banco MySQL e atualize a configuração em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tech_challenge_2
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Compilar o Projeto
```bash
mvn clean compile
```

### 4. Executar Testes
```bash
mvn test
```

### 5. Executar a Aplicação
```bash
mvn spring-boot:run
```

A aplicação será iniciada em `http://localhost:8080`

### 6. Implantação com Docker (Opcional)
```bash
# Construir imagem Docker
docker build -t tech-challenge-2 .

# Executar com Docker Compose
docker-compose up -d
```

## 📚 Documentação da API

Uma vez que a aplicação esteja rodando, você pode acessar o Swagger UI em:
`http://localhost:8080/swagger-ui.html`

## 🔗 Endpoints da API

### 1. Gerenciamento de Usuários

#### Criar Usuário
```http
POST /api/v1/usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "login": "joao123",
  "senha": "senha123",
  "tipoUsuarioId": 1,
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234567"
  }
}
```

#### Listar Todos os Usuários
```http
GET /api/v1/usuarios
```

#### Obter Usuário por ID
```http
GET /api/v1/usuarios/{id}
```

#### Atualizar Usuário
```http
PUT /api/v1/usuarios/{id}
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@email.com",
  "senha": "novaSenha123",
  "endereco": {
    "logradouro": "Nova Rua",
    "numero": "456",
    "complemento": "Casa",
    "bairro": "Vila Nova",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "04567890"
  }
}
```

#### Deletar Usuário
```http
DELETE /api/v1/usuarios/{id}
```

#### Autenticar Usuário
```http
POST /api/v1/usuarios/login
Content-Type: application/json

{
  "login": "joao123",
  "senha": "senha123"
}
```

### 2. Gerenciamento de Tipos de Usuário

#### Criar Tipo de Usuário
```http
POST /api/v1/tipos-usuario
Content-Type: application/json

{
  "nome": "Administrador"
}
```

#### Listar Todos os Tipos de Usuário
```http
GET /api/v1/tipos-usuario
```

#### Obter Tipo de Usuário por ID
```http
GET /api/v1/tipos-usuario/{id}
```

#### Atualizar Tipo de Usuário
```http
PUT /api/v1/tipos-usuario/{id}
Content-Type: application/json

{
  "nome": "Gerente"
}
```

#### Deletar Tipo de Usuário
```http
DELETE /api/v1/tipos-usuario/{id}
```

### 3. Gerenciamento de Restaurantes

#### Criar Restaurante
```http
POST /api/v1/restaurantes
Content-Type: application/json

{
  "nome": "Restaurante Italiano",
  "endereco": {
    "logradouro": "Rua da Gastronomia",
    "numero": "100",
    "complemento": "Loja 1",
    "bairro": "Centro Gastronômico",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234567"
  },
  "tipoCozinha": "Italiana",
  "horarioFuncionamento": "12:00-22:00",
  "donoId": 1
}
```

#### Listar Todos os Restaurantes
```http
GET /api/v1/restaurantes
```

#### Obter Restaurante por ID
```http
GET /api/v1/restaurantes/{id}
```

#### Atualizar Restaurante
```http
PUT /api/v1/restaurantes/{id}
Content-Type: application/json

{
  "nome": "Restaurante Italiano Premium",
  "endereco": {
    "logradouro": "Avenida Gourmet",
    "numero": "200",
    "complemento": "Loja 5",
    "bairro": "Distrito Gastronômico",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "04567890"
  },
  "tipoCozinha": "Italiana Premium",
  "horarioFuncionamento": "11:00-23:00",
  "donoId": 1
}
```

#### Deletar Restaurante
```http
DELETE /api/v1/restaurantes/{id}
```

### 4. Gerenciamento de Itens do Cardápio

#### Criar Item do Cardápio
```http
POST /api/v1/itens-cardapio
Content-Type: application/json

{
  "nome": "Pizza Margherita",
  "descricao": "Pizza tradicional italiana com molho de tomate, mussarela e manjericão",
  "preco": 25.90,
  "disponivelApenasNoLocal": false,
  "fotoPath": "/fotos/pizza-margherita.jpg",
  "restauranteId": 1
}
```

#### Listar Todos os Itens do Cardápio
```http
GET /api/v1/itens-cardapio
```

#### Obter Item do Cardápio por ID
```http
GET /api/v1/itens-cardapio/{id}
```

#### Atualizar Item do Cardápio
```http
PUT /api/v1/itens-cardapio/{id}
Content-Type: application/json

{
  "nome": "Pizza Margherita Premium",
  "descricao": "Pizza italiana premium com ingredientes selecionados",
  "preco": 32.50,
  "disponivelApenasNoLocal": true,
  "fotoPath": "/fotos/pizza-margherita-premium.jpg",
  "restauranteId": 1
}
```

#### Deletar Item do Cardápio
```http
DELETE /api/v1/itens-cardapio/{id}
```

## 📊 Respostas da API

### Resposta de Sucesso
```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": {
    // Dados da entidade
  }
}
```

### Resposta de Erro
```json
{
  "success": false,
  "message": "Mensagem de erro",
  "errors": [
    {
      "field": "campo",
      "message": "Descrição do erro"
    }
  ]
}
```

## 🧪 Testes

### Executando Testes
```bash
# Executar todos os testes
mvn test

# Executar classe de teste específica
mvn test -Dtest=CreateUsuarioUseCaseTest

# Executar com cobertura
mvn jacoco:report
```

### Cobertura de Testes
- **Cobertura Geral**: 75%
- **Total de Testes**: 192 testes
- **Testes Unitários**: Lógica de domínio, casos de uso e serviços
- **Testes de Integração**: Testes de repositório e controladores

### Estrutura dos Testes
- **Testes Unitários**: Lógica de domínio, casos de uso e serviços
- **Testes de Integração**: Testes de repositório e controladores
- **Cobertura de Testes**: Integração com Jacoco para relatórios de cobertura

## 🏛️ Benefícios da Clean Architecture

### 1. **Independência de Frameworks**
- A camada de domínio é completamente independente do Spring Boot
- A lógica de negócio pode ser testada sem dependências de framework

### 2. **Testabilidade**
- Cada camada pode ser testada isoladamente
- A lógica de domínio é pura e facilmente testável
- Os casos de uso são testáveis sem dependências de banco de dados

### 3. **Independência da UI**
- Os controladores são finos e só lidam com preocupações HTTP
- A lógica de negócio está na camada de domínio
- A UI pode ser alterada sem afetar a lógica de negócio

### 4. **Independência do Banco de Dados**
- As entidades de domínio são objetos Java puros
- As interfaces de repositório definem contratos de acesso a dados
- Os detalhes de implementação do banco de dados estão isolados

### 5. **Independência de Agências Externas**
- O domínio não depende de serviços externos
- As preocupações de infraestrutura estão isoladas
- Fácil de mockar dependências externas

## 🛡️ Segurança

- Hash de senhas usando algoritmos seguros
- Validação e sanitização de entrada
- Tratamento de exceções para erros relacionados à segurança
- Endpoint de autenticação para login de usuários

## 📊 Detalhes da Estrutura do Projeto

### Camada de Domínio
- **Entidades**: Objetos de negócio puros sem dependências de framework
- **Value Objects**: Email e Login como conceitos de domínio
- **Serviços de Domínio**: Lógica de negócio que não pertence às entidades
- **Interfaces de Repositório**: Contratos de acesso a dados

### Camada de Aplicação
- **Casos de Uso**: Regras de negócio específicas da aplicação
- **DTOs**: Objetos de transferência de dados para comunicação da API
- **Mappers**: Transformação de objetos entre camadas

### Camada de Infraestrutura
- **Entidades JPA**: Implementações específicas do banco de dados
- **Implementações de Repositório**: Implementações concretas de acesso a dados
- **Configuração**: Configurações específicas de framework

### Camada de Interface
- **Controladores**: Manipulação de requisições/respostas HTTP
- **Tratadores de Exceção**: Tratamento centralizado de erros

## 🚀 Implantação

### Implantação em Produção
1. Compilar a aplicação: `mvn clean package`
2. Executar o JAR: `java -jar target/tech_challenge_2-0.0.1-SNAPSHOT.jar`

### Implantação com Docker
```bash
# Construir e executar com Docker Compose
docker-compose up -d

# Ou construir e executar manualmente
docker build -t tech-challenge-2 .
docker run -p 8080:8080 tech-challenge-2
```

## 🤝 Contribuindo

1. Faça um fork do repositório
2. Crie uma branch para sua feature: `git checkout -b feature/nova-funcionalidade`
3. Commit suas mudanças: `git commit -m 'Adiciona nova funcionalidade'`
4. Push para a branch: `git push origin feature/nova-funcionalidade`
5. Abra um Pull Request

## 📝 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
