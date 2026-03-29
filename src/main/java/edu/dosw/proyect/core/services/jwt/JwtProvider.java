package edu.dosw.proyect.core.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para generar, validar y extraer información de tokens JWT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret:techcup-super-secret-key}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT con información del usuario
     */
    public String generateToken(String email, String role, Long userId) {
        return generateToken(email, role, userId, jwtExpiration);
    }

    /**
     * Genera un token JWT con expiración personalizada
     */
    public String generateToken(String email, String role, Long userId, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        log.debug("Token JWT generado para usuario: {}", email);
        return token;
    }

    /**
     * Valida si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el email del token
     */
    public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extrae el rol del token
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
    }

    /**
     * Extrae el userId del token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * Extrae la fecha de expiración del token
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * Verifica si el token está expirado
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Extrae todos los claims (información) del token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene toda la información del token como string legible
     */
    public String getTokenInfo(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return String.format(
                    "Email: %s | Role: %s | UserId: %s | Expira: %s",
                    getEmailFromToken(token),
                    getRoleFromToken(token),
                    getUserIdFromToken(token),
                    getExpirationDateFromToken(token)
            );
        } catch (Exception e) {
            return "Token inválido: " + e.getMessage();
        }
    }
}
