# Meu Primeiro App

Este é um projeto full-stack que consiste em uma aplicação de gerenciamento de pedidos com autenticação de usuários. O projeto utiliza Angular para o frontend e Spring Boot para o backend.

## 🚀 Tecnologias Utilizadas

### Frontend
- **Angular**: Framework para construção da interface do usuário.
- **TypeScript**: Linguagem base para o desenvolvimento Angular.
- **CSS**: Estilização dos componentes.
- **Vitest**: Runner de testes unitários.

### Backend
- **Spring Boot**: Framework Java para criação de microserviços.
- **Spring Security**: Gerenciamento de autenticação e autorização.
- **Spring Data JPA**: Abstração para persistência de dados.
- **PostgreSQL**: Banco de dados relacional.
- **Lombok**: Biblioteca para reduzir código boilerplate em Java.
- **Maven**: Gerenciador de dependências e automação de build.

## 📁 Estrutura do Projeto

O repositório está organizado da seguinte forma:

- `/src`: Contém o código fonte do frontend Angular.
- `/projeto/projeto`: Contém o código fonte do backend Spring Boot.
- `/public`: Ativos estáticos do frontend.

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- Node.js (versão recomendada v18+)
- Java JDK 17+
- Maven
- PostgreSQL

### Configuração do Banco de Dados
1. Certifique-se de que o PostgreSQL está em execução.
2. Crie um banco de dados chamado `Projeto01`.
3. Configure a variável de ambiente `DB_PASSWORD` com a senha do seu usuário PostgreSQL.

### Executando o Backend
1. Navegue até o diretório do backend:
   ```bash
   cd projeto/projeto
   ```
2. Execute a aplicação usando Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   O backend estará disponível em `http://localhost:8081`.

### Executando o Frontend
1. Navegue até a raiz do projeto:
   ```bash
   npm install
   ```
2. Inicie o servidor de desenvolvimento:
   ```bash
   npm start
   ```
   O frontend estará disponível em `http://localhost:4200`.

## 🧪 Testes

### Frontend
Para rodar os testes unitários do frontend:
```bash
npm test
```

### Backend
Para rodar os testes do backend:
```bash
cd projeto/projeto
./mvnw test
```

## 📝 Funcionalidades
- **Autenticação**: Registro de novos usuários e login seguro.
- **Gerenciamento de Pedidos**: Visualização e gerenciamento de pedidos.
