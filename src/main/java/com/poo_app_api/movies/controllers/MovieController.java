package com.poo_app_api.movies.controllers;
// Parte orquestadora; mapea peticiones a las diferentes rutas
// Encargada de gestionar las peticiones que nos van llegando

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
import com.poo_app_api.movies.services.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Especificar que la clase es un controlador, destinado a resolver peticiones y devolver en formato APIRest
@RestController
// Tipo de ruta que controla este controlador
@RequestMapping("/api/movies")
public class MovieController {
    // Evita tener que generar objetos para resolver las peticiones
    @Autowired
    // Con los repositorios que trabaja
    private MovieService movieService;

    // Listas todas las movies
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Visualizar una película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping("/{id}")
    // ResponseEntity<Movie>:Devuelve un estado, si es verdadero, devuelve un tipo de dato movie
    // PathVariable Long id: // Argumento ID se obtiene del encabezado
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // Crear Película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @PostMapping
    // Se devuelve un "ResponseEntity<Movie>" que indica que la operación se realizó con éxito
    // Una vez creado un elemento nuevo, se devuelve los datos en consola
    // @RequestBody: Recoge los datos de
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie savedMovie = movieService.createMovie(movie);
        // Si no hay errores, devuelve status 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    // Eliminar Película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        // Retorna: No existe ya el contenido
        return ResponseEntity.noContent().build();
    }

    // Actualizar película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @PutMapping("/{id}")
    public ResponseEntity<Movie> UpdateMovie(@PathVariable Long id,@RequestBody Movie updateM){
        Movie updateMovie = movieService.updateMovie(id, updateM);
        return ResponseEntity.ok(updateMovie);
    }

    // Votar una película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id,@PathVariable double rating){
        Movie updatedMovie = movieService.voteMovie(id, rating);
        return ResponseEntity.ok(updatedMovie);
    }
}