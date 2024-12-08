package com.poo_app_api.movies.security;

import com.poo_app_api.movies.exceptions.JwtValidationException;
import com.poo_app_api.movies.services.auth.Impl.CustomeUserDetailsService;
import com.poo_app_api.movies.services.auth.Impl.JWTUtilityServiceImpl;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//Valida la información del token
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtilityService jwtUtilityService;
    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public JWTAuthorizationFilter(JWTUtilityServiceImpl jwtUtilityService) {
        this.jwtUtilityService = jwtUtilityService;
    }
    // Maneja solicitud entrante, respuesta saliente, mecanismo para invocar el siguiente filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extraemos el token JWT de la cabecera de la petición Http ("Authorization")
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // Si el encabezado no contiene un token de tipo bearer, se pasa el control al sigueinte filtro
            filterChain.doFilter(request, response);
            // El código ya no continúa ejecutandose
            return;
        }
        // Se obtiene del token JWT la subcadena que contiene todos los caracteres excepto: "Bearer "
        String token = header.substring(7);

        try{
            //Se valida la información del token
            if (StringUtils.hasText(token) && jwtUtilityService.validarJWT(token)) {
                // Se valida el token y se obtiene los claims en el objeto token(username;fecha de generación del token; expiración del token)
                Claims claims = jwtUtilityService.parseJWT(token);
                // Se extrae especificamente el "username"
                String username = claims.getSubject();
                // Se crea el objeto userDetails que contiene todos los detalles del "username"
                UserDetails userDetails = customeUserDetailsService.loadUserByUsername(username);
                // Se carga una lista de String con los roles alojados en BDD
                List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                // Se verifica que el usuario autenticado tenga por lo menos un rol de los alojados en la BDD
                if (userRoles.contains("ROLE_Cliente") || userRoles.contains("ROLE_Admin")) {
                    // Creación del objeto UsernamePasswordAuthenticationToken, con información de autenticación del usuario
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // Se establece información adicional de la autenticación, como dirección ip o agente de usuario para hacer la solicitud, etc.
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Se establece el objeto anterior (autenticación del usuario) en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (JwtValidationException e) {
            // Si el token es inválido o ha expiado
            SecurityContextHolder.clearContext(); // Limpia el contexto de seguridad
            jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException(e.getMessage(), e) {});
            return;
        } catch (Exception e) {
            // Manejo de cualquiero otro error
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, new AuthenticationException("Error inesperado durante la autenticación", e) {});
            //e.printStackTrace(); // Solo para depuar en desarrollo
            return;
        }
            // Permite que la solicitud coninue hacie el siguiente filtro en la cadena de filtos
        filterChain.doFilter(request, response);
    }
}
