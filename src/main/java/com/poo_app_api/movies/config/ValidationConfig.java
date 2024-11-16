package com.poo_app_api.movies.config;

import com.poo_app_api.movies.services.Validation.UserValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    public UserValidation userValidation(){
        return new UserValidation();
    }
}
