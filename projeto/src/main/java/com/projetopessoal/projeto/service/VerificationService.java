package com.projetopessoal.projeto.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {

    // Usa SecureRandom para geração criptograficamente segura
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int MAX_ATTEMPTS = 5;
    private static final long CODE_EXPIRATION_MINUTES = 10;

    private final Map<String, CodeEntry> verificationCodes = new ConcurrentHashMap<>();
    private final EmailService emailService;

    public VerificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public String generateAndSendCode(String email) {
        // Gera um código de 6 dígitos com SecureRandom
        String code = String.format("%06d", SECURE_RANDOM.nextInt(1000000));
        
        // Armazena com timestamp de criação e contador de tentativas
        verificationCodes.put(email, new CodeEntry(code, Instant.now(), 0));

        // Envia o e-mail (agora apenas loga no console)
        emailService.sendVerificationCode(email, code);
        
        return code;
    }

    public boolean verifyCode(String email, String code) {
        CodeEntry entry = verificationCodes.get(email);
        
        if (entry == null) {
            return false;
        }

        // Verifica se o código expirou (10 minutos)
        if (Instant.now().isAfter(entry.createdAt().plusSeconds(CODE_EXPIRATION_MINUTES * 60))) {
            verificationCodes.remove(email);
            return false;
        }

        // Verifica se excedeu o limite de tentativas (proteção contra brute force)
        if (entry.attempts() >= MAX_ATTEMPTS) {
            verificationCodes.remove(email);
            return false;
        }

        // Incrementa o contador de tentativas
        verificationCodes.put(email, new CodeEntry(entry.code(), entry.createdAt(), entry.attempts() + 1));

        if (entry.code().equals(code)) {
            // Remove o código após o uso bem-sucedido
            verificationCodes.remove(email);
            return true;
        }

        return false;
    }

    // Record imutável para armazenar dados do código
    private record CodeEntry(String code, Instant createdAt, int attempts) {}
}
