package com.poo_app_api.movies.exceptions;
// Controlador que captura errores en ejecución
public class MovieAlreadyExistsException extends RuntimeException{
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}
