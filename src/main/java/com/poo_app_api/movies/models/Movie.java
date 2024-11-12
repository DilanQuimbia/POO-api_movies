package com.poo_app_api.movies.models;

import jakarta.persistence.*;
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
    private String titulo;
    private String descripcion;
    private int anio;
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


