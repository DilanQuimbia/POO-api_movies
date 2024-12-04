package com.poo_app_api.movies.services.auth.Impl;

import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Role;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.RoleRepository;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import com.poo_app_api.movies.services.Validation.UserValidation;
import com.poo_app_api.movies.services.auth.AuthService;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
     // Inyectar las dependencias de "Usuario"
    @Autowired
    private UsuarioRepository usuarioRepository; // Repositorio de usuarios, para acceder a la BDD

    @Autowired
    private JWTUtilityService jwtUtilityService; // Servicio para generar, validar y obtener username de JWT

    @Autowired
    private UserValidation userValidation; // Servicio de validación para los usuarios

    @Autowired
    private RoleRepository roleRepository; // Repositorio para acceder a la tabla de roles en la BDD
    // Método personalizado para manejar el inicio de sesión de un usuario
    @Override
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>(); // Mapa donde se almacenarán el JWT o errores
            // Busca al usuario por su nombre de usuario en la BDD
            Optional<Usuario> user = usuarioRepository.findByUsername(loginRequest.getUserName());
            // Si no se encuentra el usuario
            if (user.isEmpty()) {
                jwt.put("error", "Usuario no registrado!");
                return jwt;
            }
            // Se verifica que la contraseña ingresada coincida con la contraseña almacenada
            if (verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
                // Si las contraseñas coinciden, se genera un JWT para el usuario
                jwt.put("jwt", jwtUtilityService.generateJWT(user.get().getUsername()));
            } else {
                jwt.put("error", "Autenticación fallida");
            }
            return jwt;
        } catch (IllegalArgumentException e) {
            System.err.println("Error generando JWT: " + e.getMessage());
            throw new Exception("Error generando JWT", e);
        } catch (Exception e) {
            System.err.println("error: " + e.toString());
            throw new Exception("error desconocido", e);
        }
    }

    // Método para registrar un nuevo usuario en el sistema
    @Override
    public ResponseDTO register(Usuario user) throws Exception {
        try {

            // Se valida el usuario utilizando el servicio de validación
            ResponseDTO response = userValidation.validate(user);
            // Si hay errores, e retorna el DTO con los errores encontrado
            if (response.getNumOfErrors() > 0){
                return response; // Regresa directamente el DTO con los errores
            }
            // Se verifica si el usuario ya existe en la BDD
            if (usuarioRepository.findByUsername(user.getUsername()).isPresent()) {
                response.setNumOfErrors(1);
                response.addError("Usuario","Ya existe!");
                return response;
            }

            // Se asigna el rol "Cliente" por defecto, si no se especifica
            if (user.getRole() == null || user.getRole().isEmpty()) {
                // Busca el rol "Cliente" en la base de datos
                Role defaultRole = roleRepository.findByName("Cliente").orElseThrow(() -> new Exception("Rol Cliente no encontrado"));
                user.setRole(Set.of(defaultRole));
            }
            // Encriptación de la contraseña del usuario antes de almacenarla en la BDD
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword())); // Encriptación de la contraseña utilizando BCrypt
            // Se guarda el usuario en la BDD
            usuarioRepository.save(user);
            response.addError("Usuario","Creado exitosamente!");
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    // Método auxiliar para verificar que la contraseña ingresada coincida con la almacenada en la BDD
    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        // Se usa BCryptPasswordEncoder para comparar las contraseñas
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
}
