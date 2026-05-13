package com.projetopessoal.projeto.config;

import org.springframework.web.util.HtmlUtils;

public class InputSanitizer {
    public static String sanitize(String input) {
        if (input == null) return null;
        // Escapa caracteres HTML para evitar XSS
        return HtmlUtils.htmlEscape(input.trim());
    }
}
