package com.biblioteca.core;

public class MaterialAudiovisual extends RecursoBiblioteca {

    private String fechaPublicacion;
    private String categoria;
    private int duracion;
    private String autor;

    public MaterialAudiovisual(int id,
                               String titulo,
                               String autor,
                               String ubicacion,
                               int cantidadTotal,
                               int cantidadPrestada,
                               String fechaPublicacion,
                               String categoria,
                               int duracion) {
        super(id, titulo, ubicacion, cantidadTotal, cantidadPrestada);
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.categoria = categoria;
        this.duracion = duracion;
    }

    public String getAutor() {
        return autor;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getDuracion() {
        return duracion;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Audiovisual: " + getTitulo());
        System.out.println("Autor: " + autor);
        System.out.println("Fecha de Publicación: " + fechaPublicacion);
        System.out.println("Categoría: " + categoria);
        System.out.println("Duración (min): " + duracion);
        System.out.println("Ubicación Física: " + getUbicacionFisica());
        System.out.println("Cantidad Total: " + getCantidadTotal());
        System.out.println("Cantidad Prestada: " + getCantidadPrestada());
        System.out.println("Cantidad Disponible: " + getCantidadDisponible());
    }

    @Override
    public boolean isDisponible() {
        return getCantidadDisponible() > 0;
    }
}