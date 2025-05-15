package com.biblioteca.core;

import java.util.Date;

public class Usuario {
    private int id;
    private String carnet;
    private String nombre;
    private String apellido;
    private String password;
    private String rol;
    private Date fechaRegistro;

    public Usuario(int id, String carnet, String nombre, String apellido, String password, String rol) {
        this.id = id;
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.rol = rol;
        this.fechaRegistro = new Date();
    }

    public Usuario(String carnet, String nombre, String apellido, String password, String rol) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.rol = rol;
        this.fechaRegistro = new Date();
    }

    public Usuario(String carnet, String nombre, String apellido, String rol) {
        this(carnet, nombre, apellido, "", rol);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", carnet='" + carnet + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}