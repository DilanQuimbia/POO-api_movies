package com.poo_app_api.movies.services.auth.Impl;

import com.poo_app_api.movies.services.auth.JWTUtilityService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTUtilityServiceImpl implements JWTUtilityService {

    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Genera un token JWT firmado con SHA256
    @Override
    public String generateJWT(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId.toString())  // Usa el userId correctamente
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Usa HMAC SHA-256
                .compact();
    }

    // Valida un token JWT y retorna los claims si es válido
    @Override
    public Claims parseJWT(String token) {
        try {
            // Parseo el JWT y obtengo los claims
            return Jwts.parser()  // Nueva forma de usar el parser en jjwt
                    .setSigningKey(getSigningKey())  // Establece la clave de firma
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("El token ha expirado");
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido");
        }
    }
}
