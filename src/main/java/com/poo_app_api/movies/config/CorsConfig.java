package com.poo_app_api.movies.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${FRONTEND.URL}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //Url del frontend
                .allowedOrigins(frontendUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Pasar credenciales
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                // en rutas privadas
                .allowCredentials(true)
                .maxAge(3600);

        registry.addMapping("/auth/**")
                // Se aceptan todas las rutas "*"; http://localhost:4200
                .allowedOrigins("*")
                .allowedMethods("OPTIONS", "POST")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                // en las rutas públicas no tiene que pasar credenciales
                .allowCredentials(false)
                .maxAge(3600);
    }

}
