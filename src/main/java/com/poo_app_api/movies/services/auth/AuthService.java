package com.poo_app_api.movies.services.auth;

import com.poo_app_api.movies.dtos.LoginDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;

import java.util.HashMap;

public interface AuthService {
    HashMap<String, String> login(LoginDTO loginRequest) throws Exception;

    ResponseDTO register(Usuario user) throws Exception;
}
