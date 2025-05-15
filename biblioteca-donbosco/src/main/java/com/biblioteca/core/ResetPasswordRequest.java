package com.biblioteca.core;

import java.sql.Timestamp;

public class ResetPasswordRequest {

    private int id;
    private String rol;
    private String carnet;
    private String nuevaContrasena;
    private Timestamp fechaSolicitud;

    public ResetPasswordRequest(int id, String rol, String carnet, String nuevaContrasena, Timestamp fechaSolicitud) {
        this.id = id;
        this.rol = rol;
        this.carnet = carnet;
        this.nuevaContrasena = nuevaContrasena;
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    public Timestamp getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Timestamp fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "id=" + id +
                ", rol='" + rol + '\'' +
                ", carnet='" + carnet + '\'' +
                ", nuevaContrasena='" + nuevaContrasena + '\'' +
                ", fechaSolicitud=" + fechaSolicitud +
                '}';
    }
}