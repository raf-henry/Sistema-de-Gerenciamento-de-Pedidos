package com.projetopessoal.projeto.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final Key key;
    private final long accessExpiration = 1000 * 60 * 60 * 2; // 2 horas
    private final long refreshExpiration = 1000 * 60 * 60 * 24 * 7; // 7 dias

    public JwtUtils(@Value("${JWT_SECRET}") String secretKey) {
        // Garante que a chave tem pelo menos 256 bits (32 bytes) para HMAC-SHA256
        byte[] keyBytes = secretKey.getBytes();
        if (keyBytes.length < 32) {
            throw new RuntimeException("A chave JWT_SECRET deve ter pelo menos 32 caracteres (256 bits).");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gera um Access Token (curta duração) para o usuário, incluindo a versão do token.
     */
    public String generateToken(com.projetopessoal.projeto.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ver", user.getTokenVersion());
        claims.put("typ", "access");
        return createToken(claims, user.getUsername(), accessExpiration);
    }

    /**
     * Gera um Refresh Token (longa duração) para o usuário, incluindo a versão do token.
     */
    public String generateRefreshToken(com.projetopessoal.projeto.model.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ver", user.getTokenVersion());
        claims.put("typ", "refresh");
        return createToken(claims, user.getUsername(), refreshExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * Valida o token comparando o username e a versão do token com os dados atuais do banco.
     */
    public Boolean validateToken(String token, com.projetopessoal.projeto.model.User user) {
        final String username = extractUsername(token);
        final Integer version = extractClaim(token, claims -> claims.get("ver", Integer.class));
        
        // Comparação case-sensitive, verifica versão e expiração
        return (username.trim().equals(user.getUsername().trim()) && 
                version != null && version.equals(user.getTokenVersion()) && 
                !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
