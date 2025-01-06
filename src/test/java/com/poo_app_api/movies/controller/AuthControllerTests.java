package com.poo_app_api.movies.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.poo_app_api.movies.security.JwtAuthenticationEntryPoint;
import com.poo_app_api.movies.services.auth.Impl.CustomeUserDetailsService;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.poo_app_api.movies.controllers.AuthController;
import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.services.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

@WebMvcTest(AuthController.class) //  Se prueba un controlador
@AutoConfigureMockMvc(addFilters = false) // Filtro de seguridad deshabilitado durante la prueba
public class AuthControllerTests {

    @Autowired // Se inyecta MockMvc para simular peticiones HTTP
    private MockMvc mockMvc;

    @MockBean // Mock
    private AuthService authService;

    @MockBean // Mock
    private CustomeUserDetailsService customeUserDetailsService;

    @MockBean // Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean // Mock
    private JWTUtilityService jwtUtilityService;

    @InjectMocks // Se inyecta los Mocks en el controlador
    private AuthController authController;

    private LoginDTO loginRequest; // DTO para pruebas de login
    private ResponseDTO response; // DTO de respuesta de servicio

    // Se ejecuta antes de cada prueba para configurar los datos
    @BeforeEach
    void setup(){
        // Se crea una solicitud de login para usar en las pruebas
        loginRequest = new LoginDTO(); // Instancia de DTO login
        loginRequest.setUserName("DilanAC");
        loginRequest.setPassword("DF_1727d");

        response = new ResponseDTO(); // Instancia DTO de rspuesta
    }
    // Registro con datos válidos
    @DisplayName("Cuando se registra un usuario con datos válidos, se espera una creación exitosa")
    @Test
    public void when_registerUserWithValidData_expect_status201Created() throws Exception {
        // Given
        response.setNumOfErrors(0); // Sin errores
        response.addError("Usuario","Creado exitosamente!");
        given(authService.register(any(Usuario.class))).willReturn(response); // Simulación de servicio de autenticación que devuelve una respuesta exitosa
        // When y Then
        mockMvc.perform(post("/auth/register") // Simula Post
                        .contentType(MediaType.APPLICATION_JSON) // Contenido tipo JSON
                        .content("{\n" + // Contenido JSON para DTO de registro
                                "  \"username\": \"DilanAC\",\n" +
                                "  \"nombre\": \"Dilan Flores\",\n" +
                                "  \"email\": \"dilanflores.21@gmail.com\",\n" +
                                "  \"password\": \"DF_1727d\",\n" +
                                "  \"roles\": [\n" +
                                "    {\n" +
                                "      \"id\": 1,\n" +
                                "      \"name\": \"ROLE_Admin\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"id\": 2,\n" +
                                "      \"name\": \"ROLE_Cliente\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"))  // Convierte el DTO a JSON
                .andExpect(status().isCreated())  // Verifica el código de estado 201: Creado
                .andExpect(jsonPath("$.numOfErrors").value(0))  // Verifica sin errores
                .andExpect(jsonPath("$.errors.Usuario").value("Creado exitosamente!")); // Verfica mensaje exitoso
    }
    // Registro con datos inválidos
    @DisplayName("Cuando se registra un usuario con datos inválidos, se espera mensaje de error: BadRequest")
    @Test
    public void when_registerUserWithInvalidData_expect_status400BadRequest() throws Exception {
        // Given
        response.setNumOfErrors(1); // 1 error
        response.addError("Usuario","Ya existe!"); // Mensaje de error
        given(authService.register(any(Usuario.class))).willReturn(response); // Simulación de servicio de autenticación que devuelve una respuesta con errores
        // When y Then
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" + // Contenido JSON para DTO de registro
                                "  \"username\": \"DilanAC\",\n" +
                                "  \"nombre\": \"Dilan Flores\",\n" +
                                "  \"email\": \"dilanflores.21@gmail.com\",\n" +
                                "  \"password\": \"DF_1727d\",\n" +
                                "  \"roles\": [\n" +
                                "    {\n" +
                                "      \"id\": 1,\n" +
                                "      \"name\": \"ROLE_Admin\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"id\": 2,\n" +
                                "      \"name\": \"ROLE_Cliente\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}"))  // Convierte el DTO a JSON
                .andExpect(status().isBadRequest())  // Verifica el código de estado 400: BadRequest
                .andExpect(jsonPath("$.numOfErrors").value(1))  // Verifica con 1 error
                .andExpect(jsonPath("$.errors.Usuario").value("Ya existe!")); // Verifica mensaje de error
    }
    // Login con datos válidos
    @DisplayName("Cuando el usuario inicia sesión con datos válidos, se espera mensaje exitoso: Accepted")
    @Test
    public void when_loginUserWithValidData_expect_status201Accepted() throws Exception {
        // Give
        HashMap<String, String> jwt = new HashMap<>();
        jwt.put("jwt", "dummy-jwt-token"); // Generación exitoso de JWT
        given(authService.login(any(LoginDTO.class))).willReturn(jwt); // Simula el inicio de sesión que retorna la generación de JWT
        // When y Then
        mockMvc.perform(post("/auth/login") // Endpoint post
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"userName\": \"DilanAC\",\n" +
                        "  \"password\": \"DF_1727d\"\n" +
                        "}"))
                .andExpect(status().isAccepted())  // Estado de código 201: Acceso correcto
                .andExpect(jsonPath("$.jwt").value("dummy-jwt-token")); // Mensaje de login exitoso
    }
    // Login con datos inválidos
    @DisplayName("Cuando el usuario inicia sesión con datos inválidos, se espera mensaje de acceso denegado: Unauthorized")
    @Test
    public void when_loginUserWithInvalidData_expect_status401Unauthorized() throws Exception {
        // Given
        HashMap<String, String> jwt = new HashMap<>();
        jwt.put("error", "Usuario no registrado!"); // Definición de mensaje de error
        given(authService.login(any(LoginDTO.class))).willReturn(jwt); // Simula el servicio de login con un retorno de error al intentar generar JWT
        // When y Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"userName\": \"DilanAC\",\n" +
                                "  \"password\": \"DF_1727d\"\n" +
                                "}"))
                .andExpect(status().isUnauthorized())  // Estado de acceso denegado o no autorizado
                .andExpect(jsonPath("$.error").value("Usuario no registrado!")); // Mensaje de error
    }
}