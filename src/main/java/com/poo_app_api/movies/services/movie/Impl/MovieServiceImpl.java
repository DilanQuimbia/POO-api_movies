package com.poo_app_api.movies.services.movie.Impl;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.exceptions.InvalidMovieDataException;
import com.poo_app_api.movies.exceptions.MovieAlreadyExistsException;
import com.poo_app_api.movies.exceptions.MovieNotFoundException;
import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
import com.poo_app_api.movies.services.Validation.MovieValidation;
import com.poo_app_api.movies.services.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    MovieRepository movieRepository;

    @Override
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(()-> new MovieNotFoundException("Película no encontrada con id: "+ id));
    }

    @Override
    public Movie createMovie(Movie movie){
        ResponseDTO response = MovieValidation.validateMovie(movie);

        boolean titleExists = movieRepository.existsByTitulo(movie.getTitulo());
        if (titleExists) {
            throw new MovieAlreadyExistsException("El título de la película ya existe");
        }

        if(response.getNumOfErrors()> 0){
            throw new InvalidMovieDataException(response.getErrors().toString());
        }

        // Si no hay errores, se guarda la película
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Long id, Movie updateM){
        // Buscar película existente
        Movie update = movieRepository.findById(id)
                .orElseThrow(()-> new MovieNotFoundException("Película no encontrada con id: "+ id));

        // Validar los datos
        ResponseDTO response = MovieValidation.validateMovie(updateM);
        if(response.getNumOfErrors()> 0){
            throw new InvalidMovieDataException(response.getErrors().toString());
        }

        // Validar que el título no esté repetido (excepto si es el mismo de la película actual)
        boolean titleExists = movieRepository.existsByTituloAndIdNot(updateM.getTitulo(), id);
        if (titleExists) {
            throw new MovieAlreadyExistsException("La película ya existe.");
        }

        update.setTitulo(updateM.getTitulo());
        update.setDescripcion(updateM.getDescripcion());
        update.setAnio(updateM.getAnio());
        update.setVotos(updateM.getVotos());
        return movieRepository.save(update);
    }

    @Override
    public void deleteMovie(Long id){
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException("Película no encontrada con id: " + id);
        }
        movieRepository.deleteById(id);
    }

//    @Override
//    public Movie voteMovie(Long id, double rating) {
//        Movie movie = movieRepository.findById(id)
//                .orElseThrow(() -> new MovieNotFoundException("Película no encontrada con id: "+ id));
//
//        // Actualizar promedio de calificación
//        int totalVotes = movie.getVotos();
//        double newRating = ((movie.getRating() * totalVotes) + rating) / (totalVotes + 1);
//
//        movie.setRating(newRating);
//        movie.setVotos(totalVotes + 1);
//
//        return movieRepository.save(movie);
//    }
}
