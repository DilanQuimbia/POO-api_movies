package com.poo_app_api.movies.services.auth.Impl;

import com.poo_app_api.movies.exceptions.JwtValidationException;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTUtilityServiceImpl implements JWTUtilityService {
    // Variables que se inyectan desde application.properties
    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private long jwtExpiration;
    // Este método obtiene la clave secreta en formato de bytes para firmar el JWT
    private SecretKey getSigningKey() {
        // Conversión de String a bytes consistentes en diferentes plataformas y configuraciones
        byte[] KeyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        // algoritmo HMAC-SHA
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generación de token JWT firmado con SHA256
    @Override
    public String generateJWT(String userName) {
        Date now = new Date(); // Se obtiene la fecha y hora actual
        return Jwts.builder()
                .setSubject(userName)  // Se establece el username en el "subject"
                .setIssuedAt(now) // Se establece la fecha de generación del token
                .setExpiration(new Date(now.getTime() + jwtExpiration)) // Se establece la fecha de expiración del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Se firma de el token usando la clave secreta y el algoritmo HMAC SHA-256
                .compact(); // Token en formado compactado
    }

    // Pasea un JWT (Lee un token); Retorna los Claims
    @Override
    public Claims parseJWT(String token) {
            // Se parsea el JWT y se obtiene los claims(contenido del token)
        return Jwts.parser()  // Nueva forma de usar el parsear en jjwt
                .setSigningKey(getSigningKey())  // Establece la clave de firma para validar el token
                .build() // Construye el objeto parser
                .parseClaimsJws(token) // Se parsea el token y se obtiene los claims
                .getBody(); // retorna el cuerpo del token
    }

    // Validación del token
    @Override
    public Boolean validarJWT(String token) {
        try {
            //Validación del token por medio de la firma que contiene el String token(token)
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            // Si el token ha expirado
            throw new JwtValidationException("El token JWT ha expirado", e);
        } catch (JwtException e) {
            // Si el token no es válido por cualquier otro motivo
            throw new JwtValidationException("El token JWT es inválido", e);
        } catch (Exception e) {
            // Captura cualquier otro tipo de error y lo lanza como excepción personalizada
            throw new JwtValidationException("Error al validar el token JWT", e);
        }
    }
}
