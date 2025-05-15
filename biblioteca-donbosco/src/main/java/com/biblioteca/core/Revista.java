package com.biblioteca.core;

public class Revista extends RecursoBiblioteca {

    private String autor;
    private String fechaPublicacion;

    public Revista(int id,
                   String titulo,
                   String ubicacionFisica,
                   int cantidadTotal,
                   int cantidadPrestada,
                   String autor,
                   String fechaPublicacion) {
        super(id, titulo, ubicacionFisica, cantidadTotal, cantidadPrestada);
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Revista: " + getTitulo());
        System.out.println("Autor: " + autor);
        System.out.println("Fecha de Publicación: " + fechaPublicacion);
        System.out.println("Ubicación: " + getUbicacionFisica());
        System.out.println("Cantidad Total: " + getCantidadTotal());
        System.out.println("Cantidad Prestada: " + getCantidadPrestada());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible());
    }

    @Override
    public boolean isDisponible() {
        return getCantidadDisponible() > 0;
    }
}