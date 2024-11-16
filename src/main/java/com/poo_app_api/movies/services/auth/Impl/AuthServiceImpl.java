package com.poo_app_api.movies.services.auth.Impl;

import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import com.poo_app_api.movies.services.Validation.UserValidation;
import com.poo_app_api.movies.services.auth.AuthService;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
     // Inyectar las dependencias de "Usuario"
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtilityService jwtUtilityService;

    @Autowired
    private UserValidation userValidation;

    @Override
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<Usuario> user = usuarioRepository.findByEmail(loginRequest.getEmail());

            if (user.isEmpty()) {
                jwt.put("error", "Usuario no registrado!");
                return jwt;
            }
            if (verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
                jwt.put("jwt", jwtUtilityService.generateJWT(user.get().getId()));
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

    @Override
    public ResponseDTO register(Usuario user) throws Exception {
        try {
            ResponseDTO response = userValidation.validate(user);


            if (response.getNumOfErrors() > 0){
                return response;
            }
            // List en esta parte
            List<Usuario> getAllUsers = usuarioRepository.findAll();
            for (Usuario repeatFields : getAllUsers) {
                if (repeatFields != null) {
                    response.setMessage("Usuario ya existe!");
                    return response;
                }
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            usuarioRepository.save(user);
            response.setMessage("Usuario creado exitosamente!");
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
}
