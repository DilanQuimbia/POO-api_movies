package com.poo_app_api.movies.services.movie.Impl;

import com.poo_app_api.movies.models.Movie;
import com.poo_app_api.movies.repositories.MovieRepository;
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
                .orElseThrow(()-> new RuntimeException("Película no encontrada"));
    }

    @Override
    public Movie createMovie(Movie movie){
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Long id, Movie updateM){
        if(!movieRepository.existsById(id)){
            throw new RuntimeException("Película no encontrada");
        }
        updateM.setId(id);

        return movieRepository.save(updateM);
    }

    @Override
    public void deleteMovie(Long id){
        if(!movieRepository.existsById(id)){
            throw new RuntimeException("Película no encontrada");
        }
        movieRepository.deleteById(id);
    }

    @Override
    public Movie voteMovie(Long id, double rating) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Actualizar promedio de calificación
        int totalVotes = movie.getVotos();
        double newRating = ((movie.getRating() * totalVotes) + rating) / (totalVotes + 1);

        movie.setRating(newRating);
        movie.setVotos(totalVotes + 1);

        return movieRepository.save(movie);
    }
}
