package com.poo_app_api.movies.services.auth;

import com.poo_app_api.movies.models.Usuario;
import io.jsonwebtoken.Claims;


public interface JWTUtilityService {
    // Método para generar un token JWT firmado con SHA256 usando el ID de usuario
    String generateJWT(Long userId);

    // Método para parsear el JWT y obtener los claims
    Claims parseJWT(String token);
}
