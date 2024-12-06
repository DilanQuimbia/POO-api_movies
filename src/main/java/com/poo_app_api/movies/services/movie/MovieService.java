package com.poo_app_api.movies.services.movie;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    Movie createMovie(Movie movie);
    Movie updateMovie(Long id, Movie updateM);
    void deleteMovie(Long id);
//    Movie voteMovie(Long id, double rating);
}
