package com.poo_app_api.movies.repositories;

import com.poo_app_api.movies.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

     //Opción 1: Spring Data JPA genera automáticamente consultas SQL con palabras clave (convenciones predefinidas)
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);

//    // Opción 2: Consulta SQL
//    // Busca usuario por medio del email
//    // @Query nativa
//    @Query(value = "SELECT * FROM usuarios WHERE email_User = :email",nativeQuery= true)
//    // Opcional: Devuelve un usuario
//    Optional<Usuario> findByEmail(String email);
}

