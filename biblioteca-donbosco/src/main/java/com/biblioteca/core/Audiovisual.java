package com.biblioteca.core;

public class Audiovisual extends RecursoBiblioteca {

    public Audiovisual(int id, String titulo, String ubicacionFisica, int cantidadTotal, int cantidadPrestada) {
        super(id, titulo, ubicacionFisica, cantidadTotal, cantidadPrestada);
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Audiovisual:");
        System.out.println("ID: " + id);
        System.out.println("Título: " + titulo);
        System.out.println("Ubicación: " + ubicacionFisica);
        System.out.println("Cantidad total: " + cantidadTotal);
        System.out.println("Cantidad prestada: " + cantidadPrestada);
        System.out.println("Cantidad disponible: " + getCantidadDisponible());
    }

    @Override
    public boolean isDisponible() {
        return getCantidadDisponible() > 0;
    }

}