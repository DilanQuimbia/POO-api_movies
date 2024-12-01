package com.poo_app_api.movies.security;

import com.poo_app_api.movies.services.auth.Impl.CustomeUserDetailsService;
import com.poo_app_api.movies.services.auth.Impl.JWTUtilityServiceImpl;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTUtilityService jwtUtilityService;

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;  // Inyectamos el servicio de detalles de usuario

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->
                        csrf.disable()
                        )
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/listMovies").hasAnyAuthority("ROLE_Cliente","ROLE_Admin")
                                .requestMatchers(HttpMethod.GET,"/api/movie/{id}").hasAnyAuthority("ROLE_Cliente", "ROLE_Admin")
                                .requestMatchers(HttpMethod.POST,"/api/newMovie").hasAuthority("ROLE_Admin")
                                .requestMatchers(HttpMethod.DELETE,"/api/deleteMovie/{id}").hasAuthority("ROLE_Admin") // Solo para Admin
                                .requestMatchers(HttpMethod.PUT,"/api/updateMovie/{id}").hasAuthority("ROLE_Admin") // Solo para Admin
                                .anyRequest().authenticated() // Los demás requieren autenticación
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // Cuando se accede a una endpoints protegido y no tenga autorización: 401
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                                }))
                .build();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
