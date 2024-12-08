package com.poo_app_api.movies.security;

import com.poo_app_api.movies.exceptions.JwtValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Si el error es una instancia de JwtValidationException, lo tratamos de forma específica
        // Aquí se puede verificar si la excepción tiene el mensaje relacionado con JwtValidationException
        //if (authException.getMessage().contains("JWT")) { // O usa un mensaje más específico si es necesario
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("""
            {
                "error": "Autenticación fallida",
                "message": "%s",
                "status": 401
            }
            """.formatted(authException.getMessage()));
    }
}
