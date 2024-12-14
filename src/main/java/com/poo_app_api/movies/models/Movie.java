package com.poo_app_api.movies.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// JPA nos ofrece conjunto de funciones comunes para trabajar las entidades (recursos para CRUD)
// Repositorio: Conjunto de utilidades para trabajar sobre nuestro modelo

// Esta clase es una entidad; mapea una de las tablas de persistencia
@Entity
// A que tabla nos referimos; tabla exacta a mapear
@Table(name = "movies")
public class Movie {
    //Definir la clave primaria
    @Id
    //Segunda anotación: EL valor se autogenera en el sisitema de persistencia (BDD)
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @NotEmpty(message = "El título no puede estar vacío")
    @Size(min = 2, max = 100, message = "El título debe tener de 2 a 100 caracteres")
    private String titulo;

    @NotNull(message = "La descripción no puede ser nulo")
    @NotEmpty(message = "La descripción no puede estar vacío")
    @Size(min = 2, max = 500, message = "La descripción debe tener de 5 a 60 caracteres")
    private String descripcion;

    @NotNull(message = "El año no puede ser nulo")
    @Min(value = 1800, message = "El año debe ser mayor o igual a 1800")
    @Max(value = 2100, message = "El año no puede ser mayor que el año actual")
    private int anio;

    @Positive(message = "Los votos deben ser un número positivo")
    @Max(value = 10, message = "El número de votos no puede ser mayor a 10")
    private int votos;

    private double rating;
    // mapear en el sistema de persistencia; columna exacta a mapear; (se puede insertar validaciones)
    @Column(name = "image_url")
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}