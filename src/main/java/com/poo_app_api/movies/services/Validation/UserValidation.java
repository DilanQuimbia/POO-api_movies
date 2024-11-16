package com.poo_app_api.movies.services.Validation;

import com.poo_app_api.movies.dtos.ResponseDTO;
import com.poo_app_api.movies.models.Usuario;

public class UserValidation {

    public static ResponseDTO validate(Usuario user){
        ResponseDTO response = new ResponseDTO();

        response.setNumOfErrors(0);

        if (user.getNombre() == null || user.getNombre().length() < 5 || user.getNombre().length() > 60){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo Nombre no puede ser nulo, El nombre debe tener de 5 a 60 caracteres");
        }

//        if (user.getLastName() == null || user.getLastName().length() < 3 || user.getLastName().length() > 30){
//            response.setNumOfErrors(response.getNumOfErrors() + 1);
//            response.setMessage("El campo lastName no puede ser nulo, tampoco puede tener menos de 3 caracteres ni mas de 30");
//        }

        if (
                user.getEmail() == null ||
                        !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        ){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo email no es válido");
        }

        if(
                user.getPassword() == null ||
                        !user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,16}$")
        ){
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("La contraseña debe tener entre 8 y 16 caracteres, al menos un número, una minúscula y una mayúscula.");
        }

        return response;
    }
}
