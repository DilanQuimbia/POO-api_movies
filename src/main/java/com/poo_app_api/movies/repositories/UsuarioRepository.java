package com.poo_app_api.movies.repositories;

import com.poo_app_api.movies.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    // Busca de usuario por email
    // @Query nativa
    @Query(value = "SELECT * FROM usuarios WHERE email_User = :email",nativeQuery= true)
    // Puede devolver algo como no
    Optional<Usuario> findByEmail(String email);
}

