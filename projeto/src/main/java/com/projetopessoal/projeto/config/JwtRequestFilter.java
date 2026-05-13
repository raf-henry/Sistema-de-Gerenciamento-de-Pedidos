package com.projetopessoal.projeto.config;

import com.projetopessoal.projeto.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final com.projetopessoal.projeto.repository.UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public JwtRequestFilter(com.projetopessoal.projeto.service.CustomUserDetailsService userDetailsService,
            com.projetopessoal.projeto.repository.UserRepository userRepository,
            JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String tokenValue = authorizationHeader.substring(7);
            if (!tokenValue.trim().isEmpty() && tokenValue.contains(".")) {
                jwt = tokenValue;
            }
        }
        
        // Se não encontrou no header (ou o header estava inválido/vazio), tenta nos cookies
        if (jwt == null && request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt != null) {
            System.out.println("DEBUG: JWT encontrado para: " + request.getRequestURI());
            try {
                username = jwtUtils.extractUsername(jwt);
                System.out.println("DEBUG: Usuário extraído: " + username);
            } catch (Exception e) {
                System.out.println("DEBUG: Erro ao extrair JWT: " + e.getMessage());
            }
        } else {
            System.out.println("DEBUG: Nenhum JWT/Cookie encontrado na requisição para: " + request.getRequestURI());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final String finalUsername = username;
            com.projetopessoal.projeto.model.User user = this.userRepository.findByUsername(finalUsername).orElse(null);

            String typ = jwtUtils.extractClaim(jwt, claims -> (String) claims.get("typ"));

            if (user != null && "access".equals(typ) && jwtUtils.validateToken(jwt, user)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
