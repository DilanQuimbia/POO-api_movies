package com.poo_app_api.movies.repository;

import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;

    public Movie createMovie(){
        movie = new Movie();
        movie.setTitulo("test1");
        movie.setDescripcion("test_1");
        movie.setAnio(200);
        movie.setVotos(10);

        return movie;
    }

    @DisplayName("Test para crear una registro de película")
    @Test
    void when_movieIsCreated_expect_movieToBeSavedSuccessfully(){
        // Given
        Movie movie = createMovie();

        // When
        Movie movieGuardado = movieRepository.save(movie);

        // Then
        assertThat(movieGuardado).isNotNull();
        assertThat(movieGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar películas")
    @Test
    void When_MoviesAreSaved_expect_movieList(){
        // Given
        Movie movie2 = new Movie();
        movie2.setTitulo("test2");
        movie2.setDescripcion("test_2");
        movie2.setAnio(200);
        movie2.setVotos(10);
        movieRepository.save(movie2);

        Movie movie = createMovie();
        movieRepository.save(movie);

        // When
        List<Movie> listaMovie = movieRepository.findAll();

        // Then
        assertThat(listaMovie).isNotNull();
        assertThat(listaMovie).hasSize(2);
    }

    @DisplayName("Test para obtener una película por ID")
    @Test
    void when_MovieIdsProvided_expect_movieToBeDisplayedSuccessfullly(){
        //Given
        Movie movie = createMovie();
        movieRepository.save(movie);

        //when - comportamiento o accion que vamos a probar
        Movie movieBD = movieRepository.findById(movie.getId()).get();

        //then
        assertThat(movieBD).isNotNull();
    }

    @DisplayName("Test para actualizar una película")
    @Test
    void when_movieIsUpdated_expect_MovieWithSuccessulChanges(){
        //Given
        Movie movie = createMovie();
        movieRepository.save(movie);

        //when
        Movie movieGuardado = movieRepository.findById(movie.getId()).get();
        movieGuardado.setTitulo("test1_actualizado");
        movieGuardado.setDescripcion("test-1_actualizado");
        movieGuardado.setAnio(2000);
        movieGuardado.setVotos(1);
        Movie movieActualizado = movieRepository.save(movieGuardado);

        //then
        assertThat(movieActualizado.getTitulo()).isEqualTo("test1_actualizado");
        assertThat(movieActualizado.getDescripcion()).isEqualTo("test-1_actualizado");
        assertThat(movieActualizado.getAnio()).isEqualTo(2000);
        assertThat(movieActualizado.getVotos()).isEqualTo(1);
    }

    @DisplayName("Test para eliminar una película")
    @Test
    void when_movieIsDeleted_expect_movieDoesNotExist(){
        //Given
        Movie movie = createMovie();
        movieRepository.save(movie);

        //when
        movieRepository.deleteById(movie.getId());
        Optional<Movie> movieOptional = movieRepository.findById(movie.getId());

        //then
        assertThat(movieOptional).isEmpty();
    }
}
