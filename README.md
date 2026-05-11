# FinanceSys - Gestão Financeira Inteligente 🚀

![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Google Gemini](https://img.shields.io/badge/Google_Gemini-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)
![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)

FinanceSys é uma plataforma completa de gerenciamento financeiro pessoal, projetada para oferecer segurança, inteligência e facilidade de uso. O sistema permite o controle de múltiplas contas, importação automatizada de extratos via IA e análise de dados em tempo real.

---

## 🌐 Deploy em Produção
A aplicação está totalmente hospedada na nuvem:
- **Frontend:** [Vercel](https://finance-sys-front-end.vercel.app/) (Angular)
- **Backend:** [Render](https://financesys-backend-5nk1.onrender.com) (Spring Boot)
- **Banco de Dados:** [Neon](https://neon.tech/) (PostgreSQL Serverless)

---

## ✨ Funcionalidades Principais
- **🔒 Segurança Avançada:**
  - Autenticação via **JWT** (Stateless).
  - Verificação de e-mail obrigatória no cadastro via **Resend API**.
  - Proteção contra **IDOR**: Validação rigorosa de propriedade de dados em todos os endpoints.
  - Sanitização de erros para evitar vazamento de informações do servidor.
- **🤖 Inteligência Artificial (Gemini):**
  - Importação de extratos bancários em PDF.
  - Processamento inteligente que converte PDFs complexos em transações estruturadas automaticamente.
- **📊 Dashboard Dinâmico:**
  - Visualização de saldos e gastos por conta bancária.
  - Filtros reativos e estatísticas em tempo real usando Angular Signals.
- **💼 Gestão de Contas:**
  - Suporte a múltiplas instituições (Nubank, Itaú, Caixa, etc.).
  - Isolamento completo de dados entre usuários.

---

## 🛠️ Tecnologias e Arquitetura

### Frontend
- **Framework:** Angular 19+
- **Estilização:** Tailwind CSS (Modern UI/UX)
- **Estado:** Signals & Services
- **Deploy:** Vercel (CI/CD automático via GitHub)

### Backend
- **Framework:** Spring Boot 3.4+
- **Segurança:** Spring Security + JWT
- **Banco de Dados:** PostgreSQL (Neon)
- **E-mail:** Resend API (HTTP-based delivery)
- **Deploy:** Docker (Render Web Services)

---

## 🚀 Como Executar Localmente

### Pré-requisitos
- Node.js & npm
- JDK 17+
- Docker (opcional para rodar PostgreSQL local)

### Configuração
1. Clone o repositório.
2. Na pasta `projeto/`, crie um arquivo `.env` com as seguintes chaves:
   ```env
   DB_PASSWORD=sua_senha_local
   GEMINI_API_KEY=sua_chave_gemini
   JWT_SECRET=sua_chave_secreta_jwt
   RESEND_API_KEY=sua_chave_resend (opcional para local)
   ```
3. Instale as dependências do frontend:
   ```bash
   npm install
   ```
4. Inicie o ambiente de desenvolvimento:
   ```bash
   npm run dev
   ```

---

## 🛡️ Hardening e Segurança (Auditado)
O sistema passou por um processo de fortalecimento de segurança, incluindo:
- Bloqueio de portas SMTP inseguras (migração para API HTTP).
- Validação de tipos e tamanhos de arquivos em uploads (PDF max 10MB).
- Remoção de segredos hardcoded e migração para Variáveis de Ambiente.
- Implementação de limites de tentativas (*Rate Limiting*) para códigos de verificação.

---
© 2026 FinanceSys. Criado com foco em privacidade e inteligência financeira.
