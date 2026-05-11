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
            message.setTo(to);
            message.setSubject("Seu código de verificação - FinanceSys");
            message.setText("Olá!\n\nSeu código de verificação para cadastro no FinanceSys é: " + code + "\n\nEste código é válido por 10 minutos.");
            
            mailSender.send(message);
            System.out.println("E-mail real enviado para: " + to);
        } catch (Exception e) {
            System.err.println("============= ERRO SMTP =============");
            System.err.println("Falha ao enviar e-mail via SMTP.");
            System.err.println("Para: " + to);
            System.err.println("CÓDIGO (Visualizar para teste): " + code);
            System.err.println("Erro: " + e.getMessage());
            System.err.println("=====================================");
        }
    }
}
