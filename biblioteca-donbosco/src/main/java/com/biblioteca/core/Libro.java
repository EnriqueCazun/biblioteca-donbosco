package com.biblioteca.core;

public class Libro extends RecursoBiblioteca {

    private String autor;
    private String categoria;
    private String tipo;
    private String fechaPublicacion;
    private int paginas;

    public Libro(int id,
                 String titulo,
                 String ubicacionFisica,
                 int cantidadTotal,
                 int cantidadPrestada,
                 String autor,
                 String fechaPublicacion,
                 int paginas,
                 String categoria,
                 String tipo) {
        super(id, titulo, ubicacionFisica, cantidadTotal, cantidadPrestada);
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.paginas = paginas;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public int getPaginas() {
        return paginas;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Libro: " + getTitulo());
        System.out.println("Autor: " + autor);
        System.out.println("Categoría: " + categoria);
        System.out.println("Tipo: " + tipo);
        System.out.println("Fecha de Publicación: " + fechaPublicacion);
        System.out.println("Páginas: " + paginas);
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