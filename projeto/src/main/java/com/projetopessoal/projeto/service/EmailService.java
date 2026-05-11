package com.projetopessoal.projeto.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY:}")
    private String brevoApiKey;

    @Value("${BREVO_SENDER_EMAIL:noreply@financesys.com}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendVerificationCode(String to, String code) {
        if (brevoApiKey == null || brevoApiKey.isEmpty()) {
            System.err.println("============= MOCK EMAIL (BREVO) =============");
            System.err.println("API Key do Brevo não configurada.");
            System.err.println("Para: " + to + " | CÓDIGO: " + code);
            System.err.println("==============================================");
            return;
        }

        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            Map<String, Object> body = Map.of(
                "sender", Map.of("name", "FinanceSys", "email", senderEmail),
                "to", List.of(Map.of("email", to)),
                "subject", "Seu código de verificação - FinanceSys",
                "htmlContent", "<html><body><strong>Olá!</strong><br><br>Seu código de verificação para cadastro no FinanceSys é: <h2>" + code + "</h2><br>Este código é válido por 10 minutos.</body></html>"
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("E-mail enviado via Brevo para: " + to);
            } else {
                System.err.println("Falha ao enviar via Brevo: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("============= ERRO BREVO =============");
            System.err.println("Erro ao chamar API do Brevo: " + e.getMessage());
            System.err.println("CÓDIGO: " + code);
            System.err.println("======================================");
        }
    }
}
