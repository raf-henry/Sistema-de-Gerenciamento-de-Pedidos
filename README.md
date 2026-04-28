# FinanceSys - Controle de Gastos

Este é um projeto full-stack pessoal para gerenciamento de finanças pessoais. O sistema foi projetado com uma arquitetura moderna e segura, oferecendo controle detalhado de despesas simples e parceladas.

## 🚀 Tecnologias Utilizadas

### Frontend
- **Angular**: Framework SPA para a interface.
- **Design System**: Estilo premium baseado no ecossistema Expo (Pill-shaped, Inter font, Glassmorphism).
- **Segurança**: Autenticação via **JWT (JSON Web Token)** com interceptores de requisição.

### Backend
- **Spring Boot 3+**: Framework Java para a API REST.
- **Spring Security**: Proteção de endpoints e autenticação sem estado (Stateless).
- **JJWT**: Biblioteca para geração e validação de tokens JWT.
- **PostgreSQL**: Banco de dados relacional para persistência robusta.

## 🛡️ Diferenciais de Segurança
- **Autenticação JWT**: Sessões seguras e eficientes, sem necessidade de enviar credenciais em cada requisição.
- **Isolamento de Dados**: Arquitetura multi-usuário onde cada registro é vinculado a um ID único, garantindo total privacidade.
- **CORS & CSRF**: Configurações de segurança ajustadas para comunicação exclusiva entre frontend e backend.

## ✨ Novas Funcionalidades
- **Gestão de Parcelamentos**: Opção de registrar gastos parcelados com cálculo automático do valor total.
- **Gasto Fixo Mensal**: KPI dinâmico que soma o valor das parcelas atuais para planejamento financeiro.
- **Dashboard Inteligente**: Indicadores em tempo real sobre o fluxo de caixa e total de lançamentos.
- **Status de Despesa**: Identificação visual rápida entre gastos "Pagos" e "Parcelados" com badges coloridos.

## 🛠️ Como Executar o Projeto

1. **Instalação**: Execute `npm install` na raiz do projeto.
2. **Banco de Dados**: Certifique-se de que o PostgreSQL está rodando e a database `Projeto01` foi criada.
3. **Segurança (Senha)**: Crie um arquivo chamado `.env` na raiz do projeto e adicione sua senha: `DB_PASSWORD=sua_senha_aqui`.
4. **Execução**: Execute `npm run dev`. Isso iniciará o Frontend (Angular) e o Backend (Spring Boot) simultaneamente.

---
© 2026 FinanceSys Team. Todos os direitos reservados.
