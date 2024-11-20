package com.poo_app_api.movies.dtos;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseDTO {
    private int numOfErrors;
    private Map<String, String> errors = new HashMap<>();

    public int getNumOfErrors() {
        return numOfErrors;
    }

    public void setNumOfErrors(int numOfErrors) {
        this.numOfErrors = numOfErrors;
    }
    // Métodos para agregar y obtener los errores
    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
