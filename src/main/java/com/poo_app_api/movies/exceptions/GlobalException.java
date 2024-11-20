package com.poo_app_api.movies.exceptions;

import com.poo_app_api.movies.dtos.ErrorDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
//    // Datos mal formateados: Envio de datos incorrectos o mal formateados: 400
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String,Object>> handleIlegalArgumentException(IllegalArgumentException ex){
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", true);
//        response.put("message", ex.getMessage());
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    // Recurso no encontrado
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", true);
//        response.put("message", ex.getMessage());
//        response.put("status", HttpStatus.NOT_FOUND.value());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    //
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", true);
//        response.put("message", "Ocurrió un error inesperado: " + ex.getMessage());
//        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    // Manejo de errores en la BDD
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<Map<String, Object>> handleDatabaseErrors(DataIntegrityViolationException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", true);
//        response.put("message", "Error de integridad en la base de datos: " + ex.getMostSpecificCause().getMessage());
//        response.put("status", HttpStatus.CONFLICT.value());
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Código 409: Conflicto
//    }
    // Endpoint no encontrado: 404
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorDTO> handlenotFoundEx(Exception ex){
        ErrorDTO error = new ErrorDTO();
        error.setDate(new Date());
        error.setError(" API rest no encontrada!");
        error.setMessage(" API rest no encontrada! \n" + ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }
    // Creted: título ya registrado: 500
    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleMovieAlreadyExists(MovieAlreadyExistsException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setDate(new Date());
        error.setError("La película ya existe");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(error); // Código 409: Conflicto
    }
    // id: no conecta a una película:
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleMovieNotFound(MovieNotFoundException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setDate(new Date());
        error.setError("La película no existe");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
    }

    // Validación de datos en campos: 400
    @ExceptionHandler(InvalidMovieDataException.class)
    public ResponseEntity<ErrorDTO> handleInvalidData(InvalidMovieDataException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setDate(new Date());
        error.setError("Datos inválidos");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
