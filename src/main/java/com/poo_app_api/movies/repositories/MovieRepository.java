package com.poo_app_api.movies.repositories;

import com.poo_app_api.movies.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
// Interfaz extiende a JPARepository; dato: Entidad e identificador de clave primaria
public interface MovieRepository extends JpaRepository<Movie,Long> {
    // JPA nos ofrece conjunto de funciones comunes para trabajar las entidades (recursos para CRUD)
    // Repositorio: Conjunto de utilidades para trabajar sobre nuestro modelo
    // Utilizando el MovieRepository podemos acceder a todo el conjunto de funcionalidades del JPA en la entidad Movie: Busqueda, borrado
    // Enlaza las funciones y entidades
    // Podemos declarar métodos adicionales en este apartado que no incluya en JPARpository
}
