package com.biblioteca.dao;

import com.biblioteca.core.Manuscrito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManuscritoDAO {

    public boolean agregar(Manuscrito manuscrito) {
        String query = "INSERT INTO libros (titulo, autor, categoria, fecha_publicacion, tipo_material) VALUES (?, ?, ?, ?, 'manuscrito')";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, manuscrito.getTitulo());
            stmt.setString(2, manuscrito.getAutor());
            stmt.setString(3, manuscrito.getCategoria());
            stmt.setDate(4, new java.sql.Date(manuscrito.getFechaPublicacion().getTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar manuscrito: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String query = "DELETE FROM libros WHERE id = ? AND tipo_material = 'manuscrito'";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar manuscrito: " + e.getMessage());
            return false;
        }
    }

    public List<Manuscrito> getAll() {
        List<Manuscrito> manuscritos = new ArrayList<>();
        String query = "SELECT id, titulo, autor, categoria, fecha_publicacion FROM libros WHERE tipo_material = 'manuscrito'";

        try (Connection connection = Conexion.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                manuscritos.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener manuscritos: " + e.getMessage());
        }

        return manuscritos;
    }

    public Manuscrito getById(int id) {
        String query = "SELECT id, titulo, autor, categoria, fecha_publicacion FROM libros WHERE id = ? AND tipo_material = 'manuscrito'";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener manuscrito por ID: " + e.getMessage());
        }

        return null;
    }

    private Manuscrito mapResultSet(ResultSet rs) throws SQLException {
        return new Manuscrito(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("categoria"),
                rs.getDate("fecha_publicacion")
        );
    }
}