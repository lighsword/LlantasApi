package com.proyectoMaycollins.LlantasApi.Config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final Key key;
    private final long accessTokenExpirationMs;  // 15 minutos
    private final long refreshTokenExpirationMs; // 7 días

    public JwtService(
            @Value("${security.jwt.secret:defaultsecretdefaultsecretdefaultsecret}") String secret,
            @Value("${security.jwt.access-token-expiration-ms:900000}") long accessTokenExpirationMs,  // 15 min
            @Value("${security.jwt.refresh-token-expiration-ms:604800000}") long refreshTokenExpirationMs) { // 7 días
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    /**
     * Genera un Access Token (corta duración) con un JTI único
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenExpirationMs);

        // Agregar JTI (JWT ID) único para cada token
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setId(jti)  // Token único
                .addClaims(claims)
                .claim("type", "access")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera un Refresh Token (larga duración) con un JTI único
     */
    public String generateRefreshToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenExpirationMs);

        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setId(jti)
                .claim("type", "refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el subject (email) del token
     */
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extrae todos los claims del token
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el JTI (JWT ID) único del token
     */
    public String extractJti(String token) {
        return extractClaims(token).getId();
    }

    /**
     * Valida si el token ha expirado
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Valida si el token es válido
     */
    public boolean validateToken(String token, String email) {
        try {
            String tokenEmail = extractSubject(token);
            return tokenEmail.equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el tipo de token (access o refresh)
     */
    public String getTokenType(String token) {
        try {
            return extractClaims(token).get("type", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene la fecha de expiración del token
     */
    public Date getExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }
}
