package com.projetopessoal.projeto.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Async
    public void sendVerificationCode(String to, String code) {
        // Apenas loga no console do servidor para fins de auditoria interna
        System.out.println("============= MODO DEMONSTRAÇÃO =============");
        System.out.println("E-mail que seria enviado para: " + to);
        System.out.println("Código gerado: " + code);
        System.out.println("=============================================");
    }
}
