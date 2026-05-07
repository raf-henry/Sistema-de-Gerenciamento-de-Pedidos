# FinanceSys - Controle de Finanças Pessoais Inteligente

Este é um projeto full-stack para gerenciamento de finanças pessoais. O sistema foi projetado com uma arquitetura moderna e segura, oferecendo controle detalhado de receitas e despesas, leitura inteligente de extratos bancários via Inteligência Artificial e uma interface fluida.

## 🚀 Tecnologias Utilizadas

### Frontend
- **Angular**: Framework SPA para a interface usando Signals para reatividade de estado.
- **Tailwind CSS**: Estilização utilitária e componentes visuais premium (Glassmorphism e transições fluidas).
- **Design System**: UI responsiva com navegação persistente e barra lateral animada.
- **Segurança**: Autenticação via **JWT (JSON Web Token)** com interceptores HTTP.

### Backend
- **Spring Boot 3+**: API REST robusta construída em Java.
- **Spring Security**: Proteção de endpoints e sessões Stateless.
- **Integração IA (Gemini)**: Comunicação direta com a API Google Gemini para interpretação autônoma de PDFs.
- **PostgreSQL**: Banco de dados relacional.

## ✨ Principais Funcionalidades
- **Gestão de Contas Bancárias**: Controle do seu saldo em diferentes instituições financeiras (ex: Nubank, Caixa, PicPay).
- **Leitor de Extratos via IA**: O sistema processa extratos em PDF utilizando o Gemini, identificando automaticamente entradas, saídas, favorecidos e datas, poupando a digitação manual.
- **Exportação para Excel**: Geração instantânea de planilhas `.csv` separando corretamente os valores recebidos e pagos.
- **Dashboard Dinâmico**: Painel interativo com filtros por conta bancária e atualização de saldo em tempo real.
- **Transações Parceladas**: Registro automático de projeções futuras de gastos através do cálculo de parcelas.

## 🛠️ Como Executar o Projeto Localmente

1. **Dependências Frontend**: 
   Abra o terminal na raiz do projeto e instale os pacotes necessários:
   ```bash
   npm install
   ```

2. **Banco de Dados**: 
   Certifique-se de que o **PostgreSQL** está em execução na porta `5432` com o usuário `postgres` e que a database `Projeto01` foi previamente criada.

3. **Configuração das Variáveis (.env)**: 
   Crie um arquivo chamado `.env` na raiz do projeto e configure suas credenciais locais e de nuvem:
   ```env
   DB_PASSWORD=sua_senha_do_postgres_aqui
   GEMINI_API_KEY=sua_chave_de_api_do_google_gemini_aqui
   ```

4. **Iniciando a Aplicação**: 
   Execute o script de desenvolvimento que compila o frontend e o backend simultaneamente:
   ```bash
   npm run dev
   ```
   * O Frontend estará acessível em: `http://localhost:4200`
   * O Backend rodará internamente em: `http://localhost:8081`

---
© 2026 FinanceSys Team. Todos os direitos reservados.
