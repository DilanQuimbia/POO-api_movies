package com.poo_app_api.movies.controllers;
// Parte orquestadora; mapea peticiones a las diferentes rutas
// Encargada de gestionar las peticiones que nos van llegando

import com.poo_app_api.movies.dtos.MovieDTO;
import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
import com.poo_app_api.movies.services.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Especificar que la clase es un controlador, destinado a resolver peticiones y devolver en formato APIRest
@RestController
// Tipo de ruta que controla este controlador
@RequestMapping("/api")
@Tag(name = "Movie", description = "Gestión de películas")
public class MovieController {
    // Evita tener que generar objetos para resolver las peticiones
    @Autowired
    // Con los repositorios que trabaja
    private MovieService movieService;

    // Listas todas las movies
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @Operation(summary = "Listar películas",
            responses = {
                @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content())
            }
    )
    @GetMapping(value= "/listMovies", headers = "Accept=application/json")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Visualizar una película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @Operation(summary = "Visualizar película",
            responses = {
                @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content())
            }
    )
    @GetMapping(value= "/movie/{id}", headers = "Accept=application/json")
    // ResponseEntity<Movie>:Devuelve un estado, si es verdadero, devuelve un tipo de dato movie
    // PathVariable Long id: // Argumento ID se obtiene del encabezado
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // Crear Película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @Operation(summary = "Crear película",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = " Llenar los campos con datos correctos",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovieDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content())
            }
    )
    @PostMapping(value="/newMovie", headers = "Accept=application/json")
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
    @Operation(summary = "Eliminar película",
            responses = {
                @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content())
            }
    )
    @DeleteMapping(value="/deleteMovie/{id}", headers = "Accept=application/json")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        // Retorna: No existe ya el contenido
        return ResponseEntity.noContent().build();
    }

    // Actualizar película
    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
    @Operation(summary = "Actualizar película",requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Llenar los campos con datos correctos",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MovieDTO.class)
            )
    ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content())
            }
    )
    @PutMapping(value="updateMovie/{id}", headers = "Accept=application/json")
    public ResponseEntity<Movie> UpdateMovie(@PathVariable Long id,@RequestBody Movie updateM){
        Movie updateMovie = movieService.updateMovie(id, updateM);
        return ResponseEntity.ok(updateMovie);
    }

    // Votar una película
//    @CrossOrigin // Permite realizar peticiones desde otras aplicaciones
//    @GetMapping("/vote/{id}/{rating}")
//    public ResponseEntity<Movie> voteMovie(@PathVariable Long id,@PathVariable double rating){
//        Movie updatedMovie = movieService.voteMovie(id, rating);
//        return ResponseEntity.ok(updatedMovie);
//    }
}