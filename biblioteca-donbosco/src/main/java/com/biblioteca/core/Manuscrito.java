package com.biblioteca.core;

import java.util.Date;

public class Manuscrito extends RecursoBiblioteca {

    private String autor;
    private String categoria;
    private Date fechaPublicacion;

    public Manuscrito(int id, String titulo, String autor, String categoria, Date fechaPublicacion) {
        super(id, titulo, "", 1, 0);
        this.autor = autor;
        this.categoria = categoria;
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Manuscrito: " + getTitulo());
        System.out.println("Autor: " + autor);
        System.out.println("Categoría: " + categoria);
        System.out.println("Fecha de Publicación: " + fechaPublicacion);
        System.out.println("Ubicación Física: " + getUbicacionFisica());
        System.out.println("Cantidad Total: " + getCantidadTotal());
        System.out.println("Cantidad Prestada: " + getCantidadPrestada());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible());
    }

    @Override
    public boolean isDisponible() {
        return getCantidadDisponible() > 0;
    }

    @Override
    public String toString() {
        return "Manuscrito{" +
                "id=" + getId() +
                ", titulo='" + getTitulo() + '\'' +
                ", autor='" + autor + '\'' +
                ", categoria='" + categoria + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", cantidadDisponible=" + getCantidadDisponible() +
                '}';
    }
}