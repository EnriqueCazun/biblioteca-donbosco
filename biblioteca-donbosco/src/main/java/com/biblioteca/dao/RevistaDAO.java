package com.biblioteca.dao;

import com.biblioteca.core.Revista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevistaDAO {

    public boolean agregar(Revista revista) {
        String sql = "INSERT INTO libros (id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, tipo_material) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'revista')";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, revista.getId());
            stmt.setString(2, revista.getTitulo());
            stmt.setString(3, revista.getAutor());
            stmt.setString(4, revista.getFechaPublicacion());
            stmt.setString(5, revista.getUbicacionFisica());
            stmt.setInt(6, revista.getCantidadTotal());
            stmt.setInt(7, revista.getCantidadPrestada());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Revista> getAll() {
        List<Revista> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada " +
                     "FROM libros WHERE tipo_material = 'revista'";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Revista rev = new Revista(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("ubicacion_fisica"),
                    rs.getInt("cantidad_total"),
                    rs.getInt("cantidad_prestada"),
                    rs.getString("autor"),
                    rs.getString("fecha_publicacion")
                );
                lista.add(rev);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ? AND tipo_material = 'revista'";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}