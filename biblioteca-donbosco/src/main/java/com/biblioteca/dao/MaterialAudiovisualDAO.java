package com.biblioteca.dao;

import com.biblioteca.core.MaterialAudiovisual;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialAudiovisualDAO {

    public boolean agregar(MaterialAudiovisual material) {
        String sql = "INSERT INTO libros "
                   + "(id, titulo, autor, ubicacion_fisica, cantidad_total, cantidad_prestada, fecha_publicacion, tipo_material, duracion, categoria) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 'material_audiovisual', ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, material.getId());
            ps.setString(2, material.getTitulo());
            ps.setString(3, material.getAutor());
            ps.setString(4, material.getUbicacionFisica());
            ps.setInt(5, material.getCantidadTotal());
            ps.setInt(6, material.getCantidadPrestada());
            ps.setString(7, material.getFechaPublicacion());
            ps.setInt(8, material.getDuracion());
            ps.setString(9, material.getCategoria());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ? AND tipo_material = 'material_audiovisual'";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MaterialAudiovisual> getAll() {
        List<MaterialAudiovisual> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, ubicacion_fisica, cantidad_total, cantidad_prestada, fecha_publicacion, categoria, duracion "
                   + "FROM libros WHERE tipo_material = 'material_audiovisual'";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MaterialAudiovisual m = new MaterialAudiovisual(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("ubicacion_fisica"),
                    rs.getInt("cantidad_total"),
                    rs.getInt("cantidad_prestada"),
                    rs.getString("fecha_publicacion"),
                    rs.getString("categoria"),
                    rs.getInt("duracion")
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}