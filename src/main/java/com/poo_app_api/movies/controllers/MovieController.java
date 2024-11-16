package com.poo_app_api.movies.controllers;
// Parte orquestadora; mapea peticiones a las diferentes rutas
// Encargada de gestionar las peticiones que nos van llegando

import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
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
    private MovieRepository movieRepository;

    // Listas todas las movies
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Visualizar una película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping("/{id}")
    // ResponseEntity<Movie>:Devuelve un estado, si es verdadero, devuelve un tipo de dato movie
    // PathVariable Long id: // Argumento ID se obtiene del encabezado
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        // Optional: Porque puede o no que lo encuentre
        Optional<Movie> movie = movieRepository.findById(id);
        // Condicional de si lo encontró o no
        return movie.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    // Crear Película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @PostMapping
    // Se devuelve un "ResponseEntity<Movie>" que indica que la operación se realizó con éxito
    // Una vez creado un elemento nuevo, se devuelve los datos en consola
    // @RequestBody: Recoge los datos de
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    // Eliminar Película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        // Constrolar si el id no existe
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        // Eliminación exitoso
        movieRepository.deleteById(id);
        // Retorna: No existe ya el contenido
        return ResponseEntity.noContent().build();
    }

    // Actualizar película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @PutMapping("/{id}")
    public ResponseEntity<Movie> UpdateMovie(@PathVariable Long id,@RequestBody Movie updateMovie){
        // Controla si el id corresponde a una película
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        updateMovie.setId(id);
        Movie savedMovie = movieRepository.save(updateMovie);
        return ResponseEntity.ok(savedMovie);
    }

    // Votar una película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id,@PathVariable double rating){
        if(movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Optional<Movie> opcional = movieRepository.findById(id);
        Movie movie = opcional.get();
        // movie.rating: Puntuación actual del elemento
        // movie votes: Número total de votos
        double newRating = ((movie.getVotos() * movie.getRating()) + rating) / (movie.getVotos()+1);
        movie.setVotos(movie.getVotos()+1);
        movie.setRating(newRating);

        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(savedMovie);
    }
}