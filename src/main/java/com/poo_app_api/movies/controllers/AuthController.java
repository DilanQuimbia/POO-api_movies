package com.poo_app_api.movies.controllers;

import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth") // Ruta pública
public class AuthController {
    // Inyección de dependencias a través de "@Autowired" o constructor
    @Autowired
    private AuthService authService;

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
