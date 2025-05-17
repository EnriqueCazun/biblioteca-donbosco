package com.biblioteca.dao;

import com.biblioteca.core.Prestamo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static final Logger logger = LogManager.getLogger(PrestamoDAO.class);

    public List<Prestamo> getPrestamosActivosPorUsuario(String carnetUsuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql =
            "SELECT p.id, p.libro_id, p.usuario_id, p.fecha_prestamo, p.fecha_devolucion, l.titulo AS libro_titulo " +
            "FROM prestamos p " +
            "JOIN usuarios u ON p.usuario_id = u.id " +
            "JOIN libros l ON p.libro_id = l.id " +
            "WHERE u.carnet = ? AND p.fecha_devolucion IS NULL";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, carnetUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPrestamo(rs));
                }
            }
            logger.info("getPrestamosActivosPorUsuario {} → {} registros", carnetUsuario, lista.size());
        } catch (SQLException e) {
            logger.error("Error en getPrestamosActivosPorUsuario {}", carnetUsuario, e);
        }
        return lista;
    }

    public List<Prestamo> getHistorialPorUsuario(String carnetUsuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql =
            "SELECT p.id, p.libro_id, p.usuario_id, p.fecha_prestamo, p.fecha_devolucion, l.titulo AS libro_titulo " +
            "FROM prestamos p " +
            "JOIN usuarios u ON p.usuario_id = u.id " +
            "JOIN libros l ON p.libro_id = l.id " +
            "WHERE u.carnet = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, carnetUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPrestamo(rs));
                }
            }
            logger.info("getHistorialPorUsuario {} → {} registros", carnetUsuario, lista.size());
        } catch (SQLException e) {
            logger.error("Error en getHistorialPorUsuario {}", carnetUsuario, e);
        }
        return lista;
    }

    public List<Prestamo> getTodosLosPrestamos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql =
            "SELECT p.id, p.libro_id, p.usuario_id, p.fecha_prestamo, p.fecha_devolucion, l.titulo AS libro_titulo " +
            "FROM prestamos p " +
            "JOIN libros l ON p.libro_id = l.id";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPrestamo(rs));
            }
            logger.info("getTodosLosPrestamos → {} registros", lista.size());
        } catch (SQLException e) {
            logger.error("Error en getTodosLosPrestamos", e);
        }
        return lista;
    }

    /**
     * Verifica si el usuario ya tiene un préstamo activo para el libro dado.
     */
    public boolean tienePrestamoActivo(String carnetUsuario, int libroId) {
        String sql = "SELECT COUNT(*) FROM prestamos p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "WHERE u.carnet = ? AND p.libro_id = ? AND p.fecha_devolucion IS NULL";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, carnetUsuario);
            ps.setInt(2, libroId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error en tienePrestamoActivo usuario={} libroId={}", carnetUsuario, libroId, e);
        }
        // En caso de error, devolvemos true para prevenir duplicados
        return true;
    }

    public boolean crearPrestamo(String carnetUsuario, int libroId) {
        String insertPrestamo =
            "INSERT INTO prestamos (usuario_id, libro_id, fecha_prestamo) " +
            "VALUES ((SELECT id FROM usuarios WHERE carnet = ?), ?, ?)";
        String updateLibro = "UPDATE libros SET disponible = FALSE WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement psIns = con.prepareStatement(insertPrestamo);
             PreparedStatement psUpd = con.prepareStatement(updateLibro)) {

            con.setAutoCommit(false);

            psIns.setString(1, carnetUsuario);
            psIns.setInt(2, libroId);
            psIns.setDate(3, Date.valueOf(LocalDate.now()));
            int filas1 = psIns.executeUpdate();

            psUpd.setInt(1, libroId);
            int filas2 = psUpd.executeUpdate();

            con.commit();
            logger.info("crearPrestamo usuario={} libroId={} → ins={}, upd={}", carnetUsuario, libroId, filas1, filas2);
            return filas1 > 0 && filas2 > 0;
        } catch (SQLException e) {
            logger.error("Error en crearPrestamo usuario={} libroId={}", carnetUsuario, libroId, e);
            return false;
        }
    }

    public boolean registrarDevolucion(int prestamoId) {
        String updatePrestamo =
            "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ?";
        String updateLibro =
            "UPDATE libros SET disponible = TRUE WHERE id = (SELECT libro_id FROM prestamos WHERE id = ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement psPre = con.prepareStatement(updatePrestamo);
             PreparedStatement psLib = con.prepareStatement(updateLibro)) {

            con.setAutoCommit(false);

            psPre.setDate(1, Date.valueOf(LocalDate.now()));
            psPre.setInt(2, prestamoId);
            int filas1 = psPre.executeUpdate();

            psLib.setInt(1, prestamoId);
            int filas2 = psLib.executeUpdate();

            con.commit();
            logger.info("registrarDevolucion prestamoId={} → pre={}, lib={}", prestamoId, filas1, filas2);
            return filas1 > 0 && filas2 > 0;
        } catch (SQLException e) {
            logger.error("Error en registrarDevolucion prestamoId={}", prestamoId, e);
            return false;
        }
    }

    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int libroId = rs.getInt("libro_id");
        String libroTitulo = rs.getString("libro_titulo");
        int usuarioId = rs.getInt("usuario_id");

        Timestamp tp = rs.getTimestamp("fecha_prestamo");
        Timestamp td = rs.getTimestamp("fecha_devolucion");

        LocalDate fechaPrestamo = tp != null ? tp.toLocalDateTime().toLocalDate() : null;
        LocalDate fechaDevolucion = td != null ? td.toLocalDateTime().toLocalDate() : null;

        return new Prestamo(
            id,
            libroId,
            libroTitulo,
            usuarioId,
            fechaPrestamo,
            fechaDevolucion
        );
    }
}
