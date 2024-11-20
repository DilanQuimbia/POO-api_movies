package com.poo_app_api.movies.services.Validation;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Movie;

import java.util.ArrayList;

public class MovieValidation {

    public static ResponseDTO validateMovie(Movie movie) {

        ResponseDTO response = new ResponseDTO();
        response.setNumOfErrors(0);

        if (movie.getTitulo() == null || movie.getTitulo().trim().isEmpty() || movie.getTitulo().length() < 2 || movie.getTitulo().length() > 100) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.addError("titulo", "Es obligatorio.");
        }
        if (movie.getDescripcion() == null || movie.getDescripcion().trim().isEmpty() || movie.getTitulo().length() < 2 || movie.getTitulo().length() > 500) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.addError("Descripción", "Es obligatoria");
        }

        int currentYear = java.time.LocalDate.now().getYear();
        if (movie.getAnio() == 0 || movie.getAnio() < 1800 || movie.getAnio() > currentYear) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.addError("Año", "No puede ser nulo, Debe estar entre 1800 y la fecha actual");
        }
        if (movie.getVotos() <= 0 || movie.getVotos() > 10) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.addError("votos", "No pueden ser nulo, menores a 0, ni mayores a 10");

        }

        return response;
    }
}
