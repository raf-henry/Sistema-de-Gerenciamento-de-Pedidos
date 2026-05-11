package com.projetopessoal.projeto.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendVerificationCode(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@financesys.com");
            message.setTo(to);
            message.setSubject("Seu código de verificação - FinanceSys");
            message.setText("Olá!\n\nSeu código de verificação para cadastro no FinanceSys é: " + code + "\n\nEste código é válido por 10 minutos.");
            // mailSender.send(message); // Comentado para evitar travamentos em dev
            System.out.println("E-mail enviado (Simulado) para " + to + " com o código: " + code);
        } catch (Exception e) {
            // Em ambiente de desenvolvimento sem SMTP configurado, logar o código no console para facilitar os testes.
            System.err.println("============= MOCK EMAIL =============");
            System.err.println("Falha ao enviar e-mail real (SMTP não configurado).");
            System.err.println("Para: " + to);
            System.err.println("CÓDIGO DE VERIFICAÇÃO: " + code);
            System.err.println("======================================");
        }
    }
}
