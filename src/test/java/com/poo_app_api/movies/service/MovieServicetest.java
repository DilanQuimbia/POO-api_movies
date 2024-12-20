package com.poo_app_api.movies.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.exceptions.InvalidMovieDataException;
import com.poo_app_api.movies.exceptions.MovieAlreadyExistsException;
import com.poo_app_api.movies.exceptions.MovieNotFoundException;
import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
import com.poo_app_api.movies.services.Validation.MovieValidation;
import com.poo_app_api.movies.services.movie.Impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MovieServicetest {
    @Mock
    private MovieRepository movieRepository;  // Mock del repositorio

    @Mock
    private MovieValidation movieValidation; // Mock de Validaciones de los campos de película

    @InjectMocks
    private MovieServiceImpl movieService;  // Instancia del servicio, se inyecta los mocks

    private Movie movie;

    private ResponseDTO response;

    @BeforeEach
    void setup(){
        movie = new Movie();
        movie.setTitulo("Movie1");
        movie.setDescripcion("Description1");
        movie.setAnio(2020);
        movie.setVotos(5);

        response = new ResponseDTO();
    }

    @DisplayName("Test: Cuando se obtienen todas las películas, se espera que se devuelvan correctamente")
    @Test
    void when_getAllMovies_expect_moviesAreReturnedSuccessfully() {
        // Given
        Movie movie2 = new Movie();
        movie2.setTitulo("Movie2");
        movie2.setDescripcion("Description2");
        movie2.setAnio(2020);
        movie2.setVotos(5);
        given(movieRepository.findAll()).willReturn(List.of(movie,movie2)); // Lista de 2 películas presentes en el repositorio

        //When
        List<Movie> list_movies = movieService.getAllMovies(); // Se llama al método para obtener las películas

        // Then
        assertThat(list_movies).isNotNull(); // Listas de películas no es nula
        assertEquals(2, list_movies.size()); // 2 listas de películas
        assertEquals("Movie1", list_movies.get(0).getTitulo()); // título de la primera lista : "Movie1"
        assertEquals("Movie2", list_movies.get(1).getTitulo()); // título de la segunda lista : "Movie2"
    }

    @DisplayName("Test: Cuando se busca una película con ID que existe, se espera resultado exitoso")
    @Test
    void when_getMovieByIdAndMovieExists_expect_movieIsReturned() {
        // Visualización de película con ID exitosa
        // Given
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie));

        // When
        Movie movieID = movieService.getMovieById(1L); // Se llama al método para obtener película por ID

        // Then
        assertNotNull(movieID); // No es nulo
        assertEquals("Movie1", movieID.getTitulo()); // El título de la película buscada es igual a la esperada
    }

    @DisplayName("Test: Cuando se busca una película por ID que no existe, se espera una excepción MovieNotFoundException")
    @Test
    void when_getMovieByIdAndMovieNotFound_expect_throwMovieNotFoundException() {
        // Película no encontrada
        // Given
        given(movieRepository.findById(1L)).willReturn(Optional.empty()); //  película no existe

        // When
        assertThrows(MovieNotFoundException.class, () -> {
            movieService.getMovieById(1L); // Se verifica que se lanzó la excepción de película no encontrada
        });

        // Then
        verify(movieRepository,never()).save(any(Movie.class));
    }

    @DisplayName("Test: Cuando se crea una película con datos válidos, se espera que la película sea guardada exitosamente")
    @Test
    void when_createMovieWithValidData_expect_movieIsCreatedSuccessfully() {
        // Simula que el título no existe en el repositorio
        // Given
        given(movieRepository.existsByTitulo("Movie1")).willReturn(false); // Película no existe

        // When
        given(movieValidation.validateMovie(movie)).willReturn(response); // Sin errores de validación
        given(movieRepository.save(movie)).willReturn(movie); // Simula guardado de película en repositorio
        Movie movieGuardado = movieService.createMovie(movie); // Se llama al método para guardar película

        // Then
        assertThat(response.getNumOfErrors()).isEqualTo(0); // Sin errores de validación
        assertNotNull(movieGuardado); // Película se guarde correctamente
        assertEquals("Movie1", movieGuardado.getTitulo()); // Compara que el título de la película es "Movie1"
    }

    @DisplayName("Test: Cuando se crea una película con datos inválidos, se espera una excepción InvalidMovieDataException")
    @Test
    void when_createMovieWithInvalidData_expect_throwInvalidMovieDataException() {
        // Movie con título inválido
        // Given
        movie.setTitulo(" ");  // Título en blanco

        given(movieRepository.existsByTitulo(movie.getTitulo())).willReturn(false); // Película no existe

        response.setNumOfErrors(1);  // Se especifica el error
        response.addError("titulo", "Es obligatorio.");

        // When
        given(movieValidation.validateMovie(movie)).willReturn(response); // Con errores de validación
        assertThatThrownBy(() -> movieService.createMovie(movie))  // Se verifica que se lanzó la excepción
                .isInstanceOf(InvalidMovieDataException.class)  // La excepción debe ser InvalidMovieDataException
                .hasMessageContaining("{titulo=Es obligatorio.}"); // Mensaje de error específico

        // Then
        verify(movieRepository, never()).save(movie); // Se verifica que no se guardó la película
    }

    @DisplayName("Test: Cuando se intenta crear una película con un título ya existente, se espera una excepción MovieAlreadyExistsException")
    @Test
    void when_createMovieWithExistingTitle_expect_throwMovieAlreadyExistsException() {
        // Película con un título ya existente
        // Given
        given(movieRepository.existsByTitulo("Movie1")).willReturn(true); // Película ya existe

        // When
        given(movieValidation.validateMovie(movie)).willReturn(response); // Sin errores de validación
        assertThrows(MovieAlreadyExistsException.class, () -> {
            movieService.createMovie(movie); // Se verifica que se lanz´la excepción de película ya existe
        });

        // Then
        verify(movieRepository, never()).save(any(Movie.class)); // Se verifica que el repositorio nunca haya guardado ninguna película de tipo "Movie"
    }

    @DisplayName("Test: Cuando se intenta actualizar una película que no existe, se espera una excepción MovieNotFoundException")
    @Test
    void when_updateMovieAndMovieNotFound_expect_throwMovieNotFoundException() {
        // Película no existe
        Movie updateM = new Movie();
        updateM.setTitulo("Update_Title");
        updateM.setDescripcion("Updated_Description");
        updateM.setAnio(2021);
        updateM.setVotos(2);
        given(movieRepository.findById(1L)).willReturn(Optional.empty()); // Película no existe

        // When
        assertThatThrownBy(() -> movieService.updateMovie(1L, updateM))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("Película no encontrada con id: " + 1L); // Se verifica que se lanzó la excepción

        // Then
        verify(movieRepository,never()).save(updateM); // Se verifica que no se guardó la actualización
    }

    @DisplayName("Test: Cuando se intenta actualizar una película con datos inválidos, se espera una excepción InvalidMovieDataException")
    @Test
    void when_updateMovieWithInvalidData_expect_throwInvalidMovieDataException() {
        // Película con datos no válidos
        Movie updateM = new Movie();
        updateM.setTitulo("Update_Title");
        updateM.setDescripcion("Updated_Description");
        updateM.setAnio(2021);
        updateM.setVotos(2);
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie)); // Película existe


        // When
        response.setNumOfErrors(1);
        response.addError("titulo", "Es obligatorio.");
        given(movieValidation.validateMovie(updateM)).willReturn(response);

        // Then
        assertThatThrownBy(() -> movieService.updateMovie(1L, updateM))
                .isInstanceOf(InvalidMovieDataException.class)
                .hasMessageContaining("{titulo=Es obligatorio.}"); // Se verifica que se lanzó la excepción

        // Then
        verify(movieRepository,never()).save(updateM); // Se verifica que no se guardó la actualización
    }

    @DisplayName("Test: Cuando se intenta actualizar una película con un título ya existente, se espera una excepción MovieAlreadyExistsException")
    @Test
    void when_updateMovieWithExistingTitle_expect_throwMovieAlreadyExistsException() {
        // película ya registrado
        //Given
        Movie updateM = new Movie();
        updateM.setTitulo(" ");
        updateM.setDescripcion("Updated_Description");
        updateM.setAnio(2021);
        updateM.setVotos(2);
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie)); // película existe
        given(movieValidation.validateMovie(updateM)).willReturn(response); // No existe errores de validación
        given(movieRepository.existsByTituloAndIdNot(updateM.getTitulo(), 1L)).willReturn(true); // Título de película ya existe

        // When
        assertThatThrownBy(() -> movieService.updateMovie(1L, updateM))
                .isInstanceOf(MovieAlreadyExistsException.class)
                .hasMessageContaining("La película ya existe."); // Se verifica que se lanzó la excepción

        // Then
        verify(movieRepository,never()).save(updateM); // Se verifica que no se guardó la actualización
    }

    @DisplayName("Test: Cuando se actualiza una película exitosamente")
    @Test
    void when_updateMovieWithValidData_expect_movieIsUpdatedSuccessfully() {
        // Actualización exitosa
        // Given
        Movie updateM = new Movie();
        updateM.setTitulo("Update_Title");
        updateM.setDescripcion("Updated_Description");
        updateM.setAnio(2021);
        updateM.setVotos(2);
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie)); // película existe

        // When
        given(movieValidation.validateMovie(updateM)).willReturn(response); // No existe errores de validación
        given(movieRepository.existsByTituloAndIdNot(updateM.getTitulo(), 1L)).willReturn(false); // Título no está duplicado
        given(movieRepository.save(any(Movie.class))).willReturn(updateM); // Se guarda la película actualizada
        Movie updateResult = movieService.updateMovie(1L, updateM); // Se llama al método y verificar la actualiación exitosa

        // Then
        assertNotNull(updateResult); // película actualizada exitosamente
        assertEquals("Update_Title", updateResult.getTitulo()); // título actualizado exitosamente
        assertEquals("Updated_Description", updateResult.getDescripcion()); // Descripción actualizado exitosamente
        assertEquals(2021, updateResult.getAnio()); // Año actualizado exitosamente
        assertEquals(2, updateResult.getVotos()); // Votos actualizado exitosamente
    }

    @DisplayName("Test: Cuando se elimina una película existente, se espera que se elimine correctamente")
    @Test
    void when_deleteMovieAndMovieExists_expect_movieIsDeletedSuccessfully() {
        // Película si existe
        // Given
        given(movieRepository.existsById(1L)).willReturn(true); // Película existe

        // When
        movieService.deleteMovie(1L); // Eliminación de película

        // Then
        verify(movieRepository, times(1)).deleteById(1L); // Se verifica que se llamó al método deleteById del respositorio
    }

    @DisplayName("Test: Cuando se intenta eliminar una película que no existe, se espera una excepción MovieNotFoundException")
    @Test
    void when_deleteMovieAndMovieNotFound_expect_throwMovieNotFoundException() {
        // Película no existe en el repositorio
        //Given
        given(movieRepository.existsById(1L)).willReturn(false);

        // When
        assertThatThrownBy(() -> movieService.deleteMovie(1L))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining("Película no encontrada con id: " + 1L); // Se verifica que se lanzó la excepción

        // Then
        verify(movieRepository,never()).deleteById(1L); // Se verifica que no se eliminó
    }
}
