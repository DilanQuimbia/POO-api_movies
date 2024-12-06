package com.poo_app_api.movies.controllers;

import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.RegisterDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth") // Ruta pública
@Tag(name = "Auth", description = "Autenticación de usuario")
public class AuthController {
    // Inyección de dependencias a través de "@Autowired" o constructor
    @Autowired
    private AuthService authService;
    @CrossOrigin
    @Operation(summary = "Iniciar sesión",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registrarse con datos correctos",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterDTO.class) // Aquí usas el DTO de login
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content())
            }
    )
    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> addUser(@RequestBody Usuario user) throws Exception {
        ResponseDTO response = authService.register(user);

        // Si hay errores, devuelve un status 400 (Bad Request)
        if(response.getNumOfErrors() >0){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Si no hay errores, devuleve un status 201 (Created)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @CrossOrigin
    @Operation(summary = "Iniciar sesión",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "iniciar sesión con UserName y Password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class) // Aquí usas el DTO de login
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content())
            }
    )
    @PostMapping("/login")
    private ResponseEntity<HashMap<String, String>> login(@RequestBody LoginDTO loginRequest) throws Exception {
        HashMap<String, String> login = authService.login(loginRequest);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.UNAUTHORIZED);
        }
    }
}
