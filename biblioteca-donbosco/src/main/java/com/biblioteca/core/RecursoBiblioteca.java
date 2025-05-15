package com.biblioteca.core;

public abstract class RecursoBiblioteca {

    protected int id;
    protected String titulo;
    protected String ubicacionFisica;
    protected int cantidadTotal;
    protected int cantidadPrestada;

    public RecursoBiblioteca(int id, String titulo, String ubicacionFisica, int cantidadTotal, int cantidadPrestada) {
        this.id = id;
        this.titulo = titulo;
        this.ubicacionFisica = ubicacionFisica;
        this.cantidadTotal = cantidadTotal;
        this.cantidadPrestada = cantidadPrestada;
    }

    public abstract void mostrarDetalles();

    public abstract boolean isDisponible();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUbicacionFisica() {
        return ubicacionFisica;
    }

    public void setUbicacionFisica(String ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public int getCantidadPrestada() {
        return cantidadPrestada;
    }

    public void setCantidadPrestada(int cantidadPrestada) {
        this.cantidadPrestada = cantidadPrestada;
    }

    public int getCantidadDisponible() {
        return cantidadTotal - cantidadPrestada;
    }
}