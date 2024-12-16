package com.poo_app_api.movies.service;
// Clase de mockito para trabajar bajo BDD
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.poo_app_api.movies.config.TestConfig;
import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Role;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import com.poo_app_api.movies.services.auth.Impl.AuthServiceImpl;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class AuthServiceTests {

    @Mock // Crea un simulacro; Simula la interacción con la BDD
    private UsuarioRepository usuarioRepository;

    @Mock // Simula la generación de un JWT
    private JWTUtilityService jwtUtilityService; // Se simula el servicio de generación del JWT

    @InjectMocks // Inyecta los mocks en AuthServiceImpl
    private AuthServiceImpl authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Usuario usuario;
    private LoginDTO loginRequest;

    // Se ejecuta antes de cada prueba para configurar los datos
    @BeforeEach
    void setup(){
        Role roleCliente = new Role();
        roleCliente.setId(1L);
        roleCliente.setName("ROLE_Admin");

        Role roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_Cliente");

        // Se crea un usuario con roles y datos ficticios
        usuario = new Usuario();
        usuario.setNombre("Dilan Flores");
        usuario.setUsername("DilanAC");
        usuario.setEmail("dilanflores.21@gmail.com");
        usuario.setPassword("DF_1727d");
        Set<Role> roles = new HashSet<>();
        roles.add(roleCliente);
        roles.add(roleAdmin);
        usuario.setRole(roles);

        // Se crea una solicitud de login para usar en las pruebas
        loginRequest = new LoginDTO();
        loginRequest.setUserName("DilanAC");
        loginRequest.setPassword("DF_1727d");
    }

    @DisplayName("Test cuando un usuario es registrado, se espera que el usuario sea creado exitosamente")
    @Test
    void when_userIsRegistered_expect_userCreatedSuccessfully() throws Exception {
        // Given: Se prepara el entorno
        given(usuarioRepository.findByUsername(usuario.getUsername()))
                .willReturn(Optional.empty()); // Usuario no existe en la BDD
        given(usuarioRepository.save(usuario)) // Se guarda en la BDD
                .willReturn(usuario);
        // When: Se ejecuta el método de registrar usuario
        ResponseDTO usuarioCreado = authService.register(usuario);
        // Then
        assertThat(usuarioCreado).isNotNull(); // Se verifica que la respuesta no sea nula
        assertThat(usuarioCreado.getNumOfErrors()).isEqualTo(0); // No hay errores
        verify(usuarioRepository).save(usuario); // Se verifica que el Usuario se guardó exitosamente
    }

    @DisplayName("Test cuando el usuario ya está registrado, se espera un mensaje de error")
    @Test
    void when_userAlreadyExists_expect_ErrorMessage() throws Exception {
        // Given
        given(usuarioRepository.findByUsername(usuario.getUsername())) // Usuario ya existe en la BDD
                .willReturn(Optional.of(usuario));  // El usuario ya está presente
        // When
        ResponseDTO usuarioCreado = authService.register(usuario);

        // Then
        assertThat(usuarioCreado).isNotNull();  // Se verifica que la respuesta no sea nula
        assertThat(usuarioCreado.getNumOfErrors()).isGreaterThan(0); // Mayor a 0
    }

    @DisplayName("Test cuando el usuario no está registrado, se espera mensaje de error")
    @Test
    void when_userNotFound_expect_errorMessage() throws Exception {
        // Given
        given(usuarioRepository.findByUsername(loginRequest.getUserName()))
                .willReturn(Optional.empty()); // El usuario no está registrado
        // When
        HashMap<String, String> result = authService.login(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("error")).isEqualTo("Usuario no registrado!");
    }

    @DisplayName("Test cuando la contraseña es incorrecta, se espera mensaje de error")
    @Test
    void when_incorrectPassword_expect_errorMessage() throws Exception {
        // Given
        usuario.setPassword(passwordEncoder.encode("DF_1727d"));// Encriptar la contraseña para simular el almacenamiento en la BDD
        loginRequest.setPassword("DF_172715"); // Password solicitado; Inicio de sesión
        given(usuarioRepository.findByUsername(loginRequest.getUserName()))
                .willReturn(Optional.of(usuario)); // El usuario existe en la base de datos
        // When
        HashMap<String, String> result = authService.login(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("error")).isEqualTo("Autenticación fallida"); // Contraseña no es correcta
    }

    @DisplayName("Test cuando el login es exitoso, se espera JWT")
    @Test
    void when_loginIsSuccessful_expect_JWT() throws Exception {
        // Given
        usuario.setPassword(passwordEncoder.encode("DF_1727d"));// Encriptar la contraseña para simular el almacenamiento en la BDD
        given(usuarioRepository.findByUsername(loginRequest.getUserName()))
                .willReturn(Optional.of(usuario)); // El usuario existe
        given(jwtUtilityService.generateJWT(usuario.getUsername())) // Se simula la generación de un JWT
                .willReturn("dummy-jwt-token");

        // When
        HashMap<String, String> result = authService.login(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("jwt")).isEqualTo("dummy-jwt-token"); // Se verifica que el JWT generado sea el espeerado
        // Se verifica que el método generateJWT haya sido llamado con el nombre de usuario correcto
        verify(jwtUtilityService).generateJWT(usuario.getUsername());
    }
}
