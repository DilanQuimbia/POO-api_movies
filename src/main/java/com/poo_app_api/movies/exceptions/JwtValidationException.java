package com.poo_app_api.movies.exceptions;

public class JwtValidationException extends RuntimeException {
  public JwtValidationException(String message) {
    super(message);
  }
  public JwtValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
