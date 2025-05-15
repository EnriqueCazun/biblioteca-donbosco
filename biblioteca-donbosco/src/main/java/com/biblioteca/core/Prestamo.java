package com.biblioteca.core;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private int libroId;
    private String libroTitulo;
    private int usuarioId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public Prestamo(int id, int libroId, String libroTitulo, int usuarioId,
                    LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.id = id;
        this.libroId = libroId;
        this.libroTitulo = libroTitulo;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Prestamo(int id, int libroId, String libroTitulo, int usuarioId,
                    LocalDate fechaPrestamo) {
        this(id, libroId, libroTitulo, usuarioId, fechaPrestamo, null);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLibroId() { return libroId; }
    public void setLibroId(int libroId) { this.libroId = libroId; }

    public String getLibroTitulo() { return libroTitulo; }
    public void setLibroTitulo(String libroTitulo) { this.libroTitulo = libroTitulo; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() {
        return (this.fechaDevolucion == null) ? "ACTIVO" : "DEVUELTO";
    }

    @Override
    public String toString() {
        return "Prestamo{" +
               "id=" + id +
               ", libroId=" + libroId +
               ", libroTitulo='" + libroTitulo + '\'' +
               ", usuarioId=" + usuarioId +
               ", fechaPrestamo=" + fechaPrestamo +
               ", fechaDevolucion=" + fechaDevolucion +
               ", estado='" + getEstado() + '\'' +
               '}';
    }
}