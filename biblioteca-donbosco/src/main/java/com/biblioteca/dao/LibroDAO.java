package com.biblioteca.dao;

import com.biblioteca.core.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> getAllLibros() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, paginas, categoria, tipo_material FROM libros";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean agregar(Libro libro) {
        String sql = "INSERT INTO libros (id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, paginas, categoria, tipo_material) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, libro.getId());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getFechaPublicacion());
            ps.setString(5, libro.getUbicacionFisica());
            ps.setInt(6, libro.getCantidadTotal());
            ps.setInt(7, libro.getCantidadPrestada());
            ps.setInt(8, libro.getPaginas());
            ps.setString(9, libro.getCategoria());
            ps.setString(10, libro.getTipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean agregarLibro(Libro libro) {
        return agregar(libro);
    }

    public Libro getLibroPorId(int id) {
        String sql = "SELECT id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, paginas, categoria, tipo_material FROM libros WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLibro(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Libro> getDisponibles() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, paginas, categoria, tipo_material FROM libros WHERE cantidad_total - cantidad_prestada > 0";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return buscarPorCampo("titulo", titulo);
    }

    public List<Libro> buscarPorAutor(String autor) {
        return buscarPorCampo("autor", autor);
    }

    public List<Libro> buscarPorCategoria(String categoria) {
        return buscarPorCampo("categoria", categoria);
    }

    private List<Libro> buscarPorCampo(String campo, String valor) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, fecha_publicacion, ubicacion_fisica, cantidad_total, cantidad_prestada, paginas, categoria, tipo_material FROM libros WHERE " + campo + " LIKE ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + valor + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearLibro(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarCantidadPrestada(int id, int nuevaCantidadPrestada) {
        String sql = "UPDATE libros SET cantidad_prestada = ? WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidadPrestada);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarLibro(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return new Libro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("ubicacion_fisica"),
            rs.getInt("cantidad_total"),
            rs.getInt("cantidad_prestada"),
            rs.getString("autor"),
            rs.getString("fecha_publicacion"),
            rs.getInt("paginas"),
            rs.getString("categoria"),
            rs.getString("tipo_material")
        );
    }
}