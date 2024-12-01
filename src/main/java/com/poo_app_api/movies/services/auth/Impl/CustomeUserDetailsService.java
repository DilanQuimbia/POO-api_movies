package com.poo_app_api.movies.services.auth.Impl;

import com.poo_app_api.movies.models.Role;
import com.poo_app_api.movies.models.Usuario;
import com.poo_app_api.movies.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CustomeUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CustomeUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
//    // Lista de autoridades por medio de una lista de roles; Su uso si no se implementa UserDetails en "Usuario"
//    public Collection<GrantedAuthority> mapToAuthorities(Set<Role> role){
//        return role.stream().map(roles -> new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList());
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
