# Sistema de Gerencimanto de Pedidos

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
- `/projeto`: Contém o código fonte do backend Spring Boot.
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

### ⚡ Executando Ambos (Simultaneamente)
Para facilitar o desenvolvimento, você pode iniciar o frontend e o backend com um único comando na raiz do projeto:

1. Instale as dependências:
   ```bash
   npm install
   ```
2. **Configuração da Senha:** O projeto espera uma variável de ambiente `DB_PASSWORD`. Você pode configurar isso diretamente no `package.json` no script `dev` ou definir no seu sistema.
3. Execute o comando:
   ```bash
   npm run dev
   ```

> **⚠️ Aviso:** Certifique-se de que a senha no script `dev` do `package.json` coincide com a senha do seu usuário PostgreSQL local. Se preferir não deixar a senha no arquivo, defina a variável de ambiente `DB_PASSWORD` no seu sistema operacional.

### Executando Manualmente

#### Backend
1. Navegue até o diretório do backend:
   ```bash
   cd projeto
   ```
2. Execute a aplicação usando Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

#### Frontend
1. Na raiz do projeto:
   ```bash
   npm start
   ```

## 🧪 Testes

### Frontend
Para rodar os testes unitários do frontend:
```bash
npm test
```

### Backend
Para rodar os testes do backend:
```bash
cd projeto
./mvnw test
```

## 📝 Funcionalidades
- **Autenticação**: Registro de novos usuários e login seguro.
- **Gerenciamento de Pedidos**: Visualização e gerenciamento de pedidos.
