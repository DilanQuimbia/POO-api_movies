package com.poo_app_api.movies.services.user.Impl;

import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import com.poo_app_api.movies.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//Contiene la lógica de los métodos creados en la interfaz UserServie
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAllUsers(){
        return usuarioRepository.findAll();
    }
}
