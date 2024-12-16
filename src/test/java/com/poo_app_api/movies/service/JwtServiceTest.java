package com.poo_app_api.movies.service;

// Clase de mockito para trabajar bajo BDD
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.poo_app_api.movies.exceptions.JwtValidationException;
import com.poo_app_api.movies.models.Role;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.services.auth.Impl.JWTUtilityServiceImpl;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

@TestPropertySource(properties = {
        "application.jwt.secret-key=${JWT_SECRET}",
        "application.jwt.expiration=${EXPIRATION}"
})
@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private String token;
    private Usuario usuario;
    @Mock
    private JWTUtilityService jwtUtilityService;

    @InjectMocks
    private JWTUtilityServiceImpl jwtUtilityServiceImpl;

    @BeforeEach
    public void setup() {
        Role roleCliente = new Role();
        roleCliente.setId(1L);
        roleCliente.setName("ROLE_Admin");

        Role roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_Cliente");

        usuario = new Usuario();
        usuario.setNombre("Dilan Flores");
        usuario.setUsername("DilanAC");
        usuario.setEmail("dilanflores.21@gmail.com");
        usuario.setPassword("DF_1727d");
        Set<Role> roles = new HashSet<>();
        roles.add(roleCliente);
        //roles.add(roleAdmin);
        usuario.setRole(roles);
    }

    @DisplayName("Test cuando el token es generado, se espera que este se genera exitosamente ")
    @Test
    public void when_JwtIsGenerated_expect_jwtToBeGeneratedSuccessfully() {
        //Given
        given(jwtUtilityService.generateJWT(usuario.getUsername())).willReturn("dummy-jwt-token");
        // When
        String jwt = jwtUtilityService.generateJWT(usuario.getUsername());

        // Then
        assertThat(jwt).isNotNull(); // El token no debe ser nulo
        assertThat(jwt).isEqualTo("dummy-jwt-token");
        verify(jwtUtilityService).generateJWT(usuario.getUsername()); // Se verifica que el método fue llamado
    }

    @DisplayName("Test cuando el token es validado, se espera que retorne verdadero ")
    @Test
    void when_JwtIsValidated_expect_JwtReturnTrue() {
        // Given
        given(jwtUtilityService.validarJWT("valid-jwt-token")).willReturn(true);

        // When
        Boolean isValid = jwtUtilityService.validarJWT("valid-jwt-token");

        // Then
        assertThat(isValid).isTrue(); // El token debe ser válido
    }

    @DisplayName("Test cuando el token JWT ha expirado, se espera una excepción")
    @Test
    void when_tokenIsExpired_expect_Exception() {
        // Given
        String expiredToken = "expired-jwt-token";
        // Simula la validación de un token expirado
        given(jwtUtilityService.validarJWT(expiredToken)).willThrow(new JwtValidationException("El token JWT ha expirado"));

        // When & Then
        assertThatThrownBy(() -> jwtUtilityService.validarJWT(expiredToken))
                .isInstanceOf(JwtValidationException.class)
                .hasMessage("El token JWT ha expirado");
    }

    @DisplayName("Test cuando el token JWT es inválido, se espera una excepción")
    @Test
    void when_tokenIsInvalid_expect_Exception() {
        // Given
        String invalidToken = "invalid-jwt-token";
        // Simula la validación de un token inválido
        given(jwtUtilityService.validarJWT(invalidToken)).willThrow(new JwtValidationException("El token JWT es inválido"));

        // When & Then
        assertThatThrownBy(() -> jwtUtilityService.validarJWT(invalidToken))
                .isInstanceOf(JwtValidationException.class)
                .hasMessage("El token JWT es inválido");
    }
}
