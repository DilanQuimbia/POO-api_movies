package com.poo_app_api.movies.services.user;

import com.poo_app_api.movies.models.Usuario;

import java.util.List;

// Asegura que cualquier implementación futura en "UserServiceImpl" cumpla los métodos definidos
public interface UserService {
    public List<Usuario> findAllUsers();
}
