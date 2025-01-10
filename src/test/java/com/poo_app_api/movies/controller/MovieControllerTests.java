package com.poo_app_api.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poo_app_api.movies.controllers.MovieController;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.security.JwtAuthenticationEntryPoint;
import com.poo_app_api.movies.services.auth.Impl.CustomeUserDetailsService;
import com.poo_app_api.movies.services.auth.JWTUtilityService;
import com.poo_app_api.movies.services.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class) // Se pruebas controlador de películas
@AutoConfigureMockMvc(addFilters = false) // Filtro de seguridad deshabilitado para las pruebas
public class MovieControllerTests {
    @Autowired // Se inyecta MockMvc para simular peticiones HTTP
    private MockMvc mockMvc;

    @Autowired // Para convertir objetos en JSON y viceversa
    private ObjectMapper objectMapper;

    @MockBean // Mock de servicio de películas; Simula el comportamiento del servicio
    private MovieService movieService;

    @MockBean // Mock servicio de detalle de usuario
    private CustomeUserDetailsService customeUserDetailsService;

    @MockBean // Mock autenticación JWT
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean // Mock servicio de utilidades JWT
    private JWTUtilityService jwtUtilityService;


    @InjectMocks // Se inyecta los mocks en el controlador
    private MovieController movieController;

    private Movie movie;
    private ResponseDTO response;

    // Se ejecuta antes de cada prueba para configurar los datos
    @BeforeEach
    void setup(){
        movie = new Movie(); // Instancia de película
        movie.setId(1L);
        movie.setTitulo("Movie1");
        movie.setDescripcion("Description1");
        movie.setAnio(2020);
        movie.setVotos(5);

        response = new ResponseDTO(); // Instancia para la respuesta
        response.setNumOfErrors(0); // Sin errores
        response.addError("Usuario","Creado exitosamente!");
    }

    @DisplayName("Test: Cuando se obtienen todas las películas, se espera que se devuelvan correctamente")
    @Test
    void when_getAllMovies_expect_moviesAreReturnedSuccessfully() throws Exception {
        // Given
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitulo("Movie2");
        movie2.setDescripcion("Description2");
        movie2.setAnio(2020);
        movie2.setVotos(5);

        List<Movie> list_movies = List.of(movie,movie2); // Lista de películas

        given(movieService.getAllMovies()).willReturn(list_movies); // Simula el servicio; Retorna dos películas

        // When
        ResultActions response = this.mockMvc.perform(get("/api/listMovies")); // Endpoint

        //Then
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica tipo de contenido JSON
                .andExpect(jsonPath("$[0].titulo").value("Movie1")) // Verifica primer títuo de película
                .andExpect(jsonPath("$[0].descripcion").value("Description1"))
                .andExpect(jsonPath("$[1].titulo").value("Movie2")) // Verifica segundo título de película
                .andExpect(jsonPath("$[1].descripcion").value("Description2"))
                .andExpect(jsonPath("$.size()").value(2)); // Verifica que hay 2 películas
        verify(this.movieService).getAllMovies();  // Verificar que el servicio fue llamado
    }


    @DisplayName("Test: Cuando se obtenga el id de una película, se espera que se devuelvan correctamente")
    @Test
    void when_getIdMovies_expect_moviesAreReturnedSuccessfully() throws Exception {
        // Given
        Long IdMovie = 1L;
        given(movieService.getMovieById(IdMovie)).willReturn(movie); // Simula el servicio; Retorna la película en base al ID

        // When
        ResultActions response = this.mockMvc.perform(get("/api/movie/{id}",IdMovie));

        //Then
        response.andExpect(status().isOk()) // Se verifica estado 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Se verifica contenido JSON
                .andExpect(jsonPath("$.titulo").value("Movie1")) // Se verifica titulo
                .andExpect(jsonPath("$.descripcion").value("Description1")); // Se verifica descripción
        verify(this.movieService).getMovieById(IdMovie); // Se verifica que el servicio fue llamado
        verify(this.movieService).getMovieById(anyLong()); // Se verifica que el id es de tipo long
    }

    @DisplayName("Test: Crear una película con datos válidos debería devolver una respuesta con éxito (201 Created)")
    @Test
    void when_setMovieData_expect_moviesIsCreatedSuccessfully() throws Exception {
        // Given
        given(movieService.createMovie(any(Movie.class))).willReturn(movie); // Simula servicio; Retorna una película creada
        // When
        ResultActions response = this.mockMvc.perform(post("/api/newMovie") // Enpoint post
                .contentType(MediaType.APPLICATION_JSON) // Tipo de contenido JSON
                .accept(MediaType.APPLICATION_JSON) // Se acepta la respuesta en formato JSON
                .content(objectMapper.writeValueAsString(movie))); // El cuerpo JSON para la película
        //Then
        response.andExpect(status().isCreated()) // Se verifica el código 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Se verifica contenifo JSON
                .andExpect(jsonPath("$.titulo").value("Movie1")) // Se verifica título de película
                .andExpect(jsonPath("$.descripcion").value("Description1")); // Se verfiica descipción de película
        verify(this.movieService).createMovie(any(Movie.class)); // Se verifica que el servicio se llamó corrctamente
    }

    @DisplayName("Test: Cuando se elimina una película, se espera que se retorne código 204")
    @Test
    void when_deleteMovie_expect_noContentStatus() throws Exception {
        // Given
        Long movieId = 1L;

        // When
        ResultActions response = this.mockMvc.perform(delete("/api/deleteMovie/{id}", movieId)// Endpoint delete
                .accept(MediaType.APPLICATION_JSON)); // Se acepta contenido JSON

        // Then
        response.andExpect(status().isNoContent()) // Se espera código 204 No Content
                .andExpect(content().string("")); // Se verifica que el cuerpo está vacío
        verify(movieService).deleteMovie(movieId); // Se verifica que el servicio fue llamado
    }

    @DisplayName("Test: Cuando se actualiza una película, se espera que se retorne código 200 y la película actualizada")
    @Test
    void when_updateMovie_expect_movieIsUpdatedSuccessfully() throws Exception {
        // Given
        Long movieId = 1L;

        Movie updatedMovie = new Movie(); // Actualiación: Nuevos datos de película
        updatedMovie.setId(movieId);
        updatedMovie.setTitulo("Updated Movie");
        updatedMovie.setDescripcion("Updated Description");
        updatedMovie.setAnio(2022);
        updatedMovie.setVotos(5);

        given(movieService.updateMovie(eq(movieId), any(Movie.class))).willReturn(updatedMovie); // Mock de servicio para devolver la película actualizada

        // When
        ResultActions response = this.mockMvc.perform(put("/api/updateMovie/{id}", movieId) // Endpoint put
                .contentType(MediaType.APPLICATION_JSON) // Contenido JSON
                .accept(MediaType.APPLICATION_JSON) // Se acepta la rspuesta de tipo JSON
                .content(objectMapper.writeValueAsString(updatedMovie))); // Cuerpo de la solicitud tipo JSON

        // Then
        response.andExpect(status().isOk()) // Se verifica código 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Se verifica contenido tipo JSON
                .andExpect(jsonPath("$.titulo").value("Updated Movie")) // Se verifica título actualizado
                .andExpect(jsonPath("$.descripcion").value("Updated Description")) // Se verifica descripción actualizada
                .andExpect(jsonPath("$.anio").value(2022)) // Se verifica año actualizado
                .andExpect(jsonPath("$.votos").value(5)); // Se verifica votos actualizados

        verify(movieService).updateMovie(eq(movieId), any(Movie.class)); //  Se verifica que el servicio de actualización fue llamado
    }
}


