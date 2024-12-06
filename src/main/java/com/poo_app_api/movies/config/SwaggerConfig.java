package com.poo_app_api.movies.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // Nombres para las claves de seguridad
    private static final String SECURITY_SCHEME_NAME = "Bearer-Token";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME )) // Seguridad a nivel global
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME , createJWTTokenScheme())
                )
                .info(new Info()
                        .title("Web service API")
                        .description("API de acceso para gestionar películas y roles")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dilan Flores")
                                .email("dilanflores.21@gmail.com")
//                                .url("www.emsolucion.com")
                                )
//                        .license(new License().name("Licencia de API")
//                                .url("https://example.com/licence"))
                );
    }
    // Método para crear el esquema de seguridad JWT
    private SecurityScheme createJWTTokenScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // Tipo HTTP para JWT
                .scheme("bearer") // Esquema Bearer
                .bearerFormat("JWT"); // Formato del token
    }
}
