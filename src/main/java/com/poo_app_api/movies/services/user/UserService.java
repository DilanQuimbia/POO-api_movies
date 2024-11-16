package com.poo_app_api.movies.services.user;

import com.poo_app_api.movies.models.Usuario;

import java.util.List;

public interface UserService {
    public List<Usuario> findAllUsers();
}
