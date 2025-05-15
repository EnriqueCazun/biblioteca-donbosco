package com.biblioteca.dao;

import com.biblioteca.core.MaterialReferencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialReferenciaDAO {

    public boolean agregar(MaterialReferencia material) {
        String sql = "INSERT INTO libros " +
                "(id, titulo, ubicacion_fisica, cantidad_total, cantidad_prestada, tipo_material, solo_consulta) " +
                "VALUES (?, ?, ?, ?, ?, 'referencia', ?)";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, material.getId());
            statement.setString(2, material.getTitulo());
            statement.setString(3, material.getUbicacionFisica());
            statement.setInt(4, material.getCantidadTotal());
            statement.setInt(5, material.getCantidadPrestada());
            statement.setBoolean(6, material.isSoloConsulta());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar material de referencia: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ? AND tipo_material = 'referencia'";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar material de referencia: " + e.getMessage());
            return false;
        }
    }

    public List<MaterialReferencia> getAll() {
        List<MaterialReferencia> materiales = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE tipo_material = 'referencia'";

        try (Connection connection = Conexion.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MaterialReferencia material = new MaterialReferencia(
                        resultSet.getInt("id"),
                        resultSet.getString("titulo"),
                        resultSet.getString("ubicacion_fisica"),
                        resultSet.getInt("cantidad_total"),
                        resultSet.getInt("cantidad_prestada"),
                        resultSet.getString("tipo_material"),
                        resultSet.getBoolean("solo_consulta")
                );
                materiales.add(material);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener materiales de referencia: " + e.getMessage());
        }

        return materiales;
    }
}