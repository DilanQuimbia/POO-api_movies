package com.poo_app_api.movies.services.auth;

import com.poo_app_api.movies.models.Usuario;
import io.jsonwebtoken.Claims;


public interface JWTUtilityService {
    // Método para generar un token JWT firmado con SHA256 usando el UserName de usuario
    String generateJWT(String userName);

    // Método para Obtener los claims del Token
    Claims parseJWT(String token);

    // Método para verificar un token válido
    Boolean validarJWT(String token);
}
