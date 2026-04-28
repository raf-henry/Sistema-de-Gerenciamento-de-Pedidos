# FinanceSys - Controle de Gastos (Multi-usuário)

Este é um projeto full-stack profissional para gerenciamento de finanças pessoais. O sistema foi projetado com uma arquitetura multi-usuário, onde cada usuário possui seu próprio histórico de despesas isolado e seguro.

## 🚀 Tecnologias Utilizadas

### Frontend
- **Angular**: Framework SPA para a interface.
- **Design System**: Estilo premium baseado no ecossistema Expo (Pill-shaped, Inter font, Glassmorphism).
- **Segurança**: Autenticação via Basic Auth com persistência em LocalStorage.

### Backend
- **Spring Boot 3+**: Framework Java para a API REST.
- **Spring Security**: Gerenciamento de autenticação e proteção de endpoints.
- **Spring Data JPA**: Persistência de dados com suporte a transações.
- **PostgreSQL**: Banco de dados relacional.

## 🛡️ Diferenciais de Segurança
- **Isolamento de Dados**: Cada usuário autenticado só tem acesso aos seus próprios registros (filtragem via `@AuthenticationPrincipal`).
- **Integridade**: Operações de escrita protegidas por `@Transactional`.
- **CORS**: Configuração restrita para permitir acesso apenas do frontend oficial.

## 🛠️ Como Executar o Projeto

1. **Instalação**: `npm install` na raiz.
2. **Configuração**: Ajuste a senha do PostgreSQL no `package.json` (variável `DB_PASSWORD`).
3. **Execução**: `npm run dev` (inicia Angular e Spring Boot simultaneamente).

---

## 📝 Funcionalidades
- **Registro/Login**: Sistema de contas individuais.
- **Dashboard Pessoal**: KPIs dinâmicos (Total de Lançamentos e Valor Gasto).
- **CRUD Completo**: Gestão total de despesas (Criar, Listar, Editar e Excluir).
