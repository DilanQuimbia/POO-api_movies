package com.poo_app_api.movies.security;

import com.poo_app_api.movies.services.auth.Impl.CustomeUserDetailsService;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTUtilityService jwtUtilityService; // Inyecta sevicio encargado de manejar jwt: creación, validación, obtención de claims

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;  // Inyectamos el servicio de detalles de usuario (username)
    // INyección por campo
    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter; // Inyecta el filtro de autorización que maneja la validación del token JWT en cada solicitud
    // Inyección por constructor (recomendada)
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }


    // Se configura las reglas de seguridad para las solicitudes Http entrantes
    @Bean // Para que Spring lo getione
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->
                        csrf.disable() // Con JWT no es necesario la protección CRF
                )
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll() // Accesos libre a todos los endpoints que comeinzan con "/auth/"
                                .requestMatchers("/v3/api-docs").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/listMovies").hasAnyAuthority("ROLE_Cliente","ROLE_Admin") // Permite acceso a usuario Cliente o Admin
                                .requestMatchers(HttpMethod.GET,"/api/movie/{id}").hasAnyAuthority("ROLE_Cliente", "ROLE_Admin") // Permite acceso a usuario Cliente o Admin
                                .requestMatchers(HttpMethod.POST,"/api/newMovie").hasAuthority("ROLE_Admin") // Solo para Admin
                                .requestMatchers(HttpMethod.DELETE,"/api/deleteMovie/{id}").hasAuthority("ROLE_Admin") // Solo para Admin
                                .requestMatchers(HttpMethod.PUT,"/api/updateMovie/{id}").hasAuthority("ROLE_Admin") // Solo para Admin
                                .anyRequest().authenticated() // Los demás requieren autenticación
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //  Con JWT, no se utiliza sesiones; Manejo de sesiones sin estado
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Se anñade primero el filtro JWT y después el filtro de autenticación predeterminado de Spring
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Uso de AuthenticacionEntryPoint (manej de error 401: autenticación)
                                .accessDeniedHandler((request, response, accessDeniedException) -> { // Manejo de error 403: Usuario no autorizado para acceder a un endpoint específico
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write("""
            {
                "message": "Acceso denegado",
                "error": "No tienes permiso para realizar esta acción",
                "status": 403
            }
        """);
                                })
                )
                .build(); // Construye y devuelve la configuración de seguridad


    }
    // Este método define el encoder de contraseñas utilizado para cifrar las contraseñas de los usuarios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza el algoritmo BCrypt para cifrar y verificar contraseñas
    }
}
