package com.projetopessoal.projeto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY:}")
    private String resendApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async
    public void sendVerificationCode(String to, String code) {
        // Se não houver API Key, apenas loga o código (útil para dev local)
        if (resendApiKey == null || resendApiKey.isEmpty()) {
            System.err.println("============= MOCK EMAIL (RESEND) =============");
            System.err.println("API Key do Resend não configurada.");
            System.err.println("Para: " + to + " | CÓDIGO: " + code);
            System.err.println("===============================================");
            return;
        }

        try {
            String url = "https://api.resend.com/emails";

            Map<String, Object> body = Map.of(
                "from", "FinanceSys <onboarding@resend.dev>",
                "to", to,
                "subject", "Seu código de verificação - FinanceSys",
                "html", "<strong>Olá!</strong><br><br>Seu código de verificação para cadastro no FinanceSys é: <h2>" + code + "</h2><br>Este código é válido por 10 minutos."
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("E-mail enviado via Resend para: " + to);
            } else {
                System.err.println("Falha ao enviar via Resend: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("============= ERRO RESEND =============");
            System.err.println("Erro ao chamar API do Resend: " + e.getMessage());
            System.err.println("CÓDIGO: " + code);
            System.err.println("=======================================");
        }
    }
}
