package com.projetopessoal.projeto.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${GEMINI_API_KEY:NOT_FOUND}")
    private String apiKey;

    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> processarExtratoCaixa(byte[] pdfBytes) throws Exception {
        if ("NOT_FOUND".equals(apiKey) || apiKey.startsWith("${")) {
            throw new RuntimeException("Configuração de API ausente.");
        }
        
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        String prompt = "Você é um especialista em extração de dados bancários brasileiros. " +
                "Analise este extrato da Caixa Econômica Federal e extraia TODOS os lançamentos financeiros. " +
                "O extrato possui 7 colunas: Lançamentos, Nr. Doc, Histórico/Complemento, Favorecido, CPF/CNPJ, Valor, Saldo. " +
                "Considere as seguintes regras: " +
                "1. Ignore linhas que representam apenas o Saldo Total ou Saldo do Dia (onde Nr. Doc é 000000). " +
                "2. Capture TODAS as transações (Entradas/Créditos e Saídas/Débitos). " +
                "3. Para cada transação, identifique e retorne: " +
                "   - 'descricao': O 'Histórico/Complemento' completo. " +
                "   - 'valor': O valor absoluto da transação (remova sinais). " +
                "   - 'tipo': 'RECEITA' se o valor original for crédito/positivo/entrada, ou 'DESPESA' se for débito/negativo/saída. " +
                "   - 'nrDoc': O número do documento. " +
                "   - 'favorecido': O nome do favorecido (se houver). " +
                "   - 'cpfCnpj': O CPF ou CNPJ (se houver). " +
                "   - 'saldo': O valor do saldo final após a transação (se legível). " +
                "   - 'dataGasto': A data ('Lançamentos') no formato ISO (YYYY-MM-DDTHH:MM:SS). Se não houver hora, use 12:00:00. " +
                "   - 'categoria': A categoria da transação baseada no histórico ou favorecido. Exemplos: " +
                "       'Lazer' (steam, epicgames, etc); " +
                "       'Alimentação' (iFood, Rappi, Zé Delivery, Loggi, Daki, Aiqfome, Delivery Much, James Delivery, Lalamove, Borzo, Pede.ai, Appito, Justo, Cornershop, Uber Eats, Flash Courier, Clique Retire); " +
                "       'Transporte' (postos de gasolina, Uber, 99, Indrive, Maxim, Uber Flash, 99Entrega, Wappa, MobizapSP, Bibi Mob, Garupa, Lady Driver, Stop Club, Urban, Tembici, V1, BlaBlaCar, Zarp Localiza, Sity, MeBusca). " +
                "       Use 'Outros' se não for possível classificar de acordo. " +
                "4. Responda APENAS com um array JSON válido, sem markdown ou explicações. " +
                "Exemplo de saída: [{\"descricao\": \"PIX ENVIADO\", \"valor\": 20.00, \"tipo\": \"DESPESA\", \"nrDoc\": \"123456\", \"favorecido\": \"Uber BR\", \"cpfCnpj\": \"***.123.456-**\", \"saldo\": 1500.50, \"dataGasto\": \"2024-05-01T20:26:00\", \"categoria\": \"Transporte\"}]";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt),
                                Map.of("inline_data", Map.of(
                                        "mime_type", "application/pdf",
                                        "data", base64Pdf
                                ))
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        // Usar URI previne que o RestTemplate faça double-encoding no sinal de ":"
        java.net.URI uri = java.net.URI.create(GEMINI_API_URL + apiKey);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            String jsonOutput = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            
            // Limpa possíveis marcadores de markdown se a IA ignorar o comando
            jsonOutput = jsonOutput.replace("```json", "").replace("```", "").trim();
            
            return objectMapper.readValue(jsonOutput, List.class);
        } else {
            throw new RuntimeException("Erro ao chamar API do Gemini: " + response.getStatusCode());
        }
    }
}
