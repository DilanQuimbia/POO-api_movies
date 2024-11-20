package com.poo_app_api.movies.exceptions;

public class InvalidMovieDataException extends RuntimeException{
    public InvalidMovieDataException(String message){
        super(message);
    }
}
