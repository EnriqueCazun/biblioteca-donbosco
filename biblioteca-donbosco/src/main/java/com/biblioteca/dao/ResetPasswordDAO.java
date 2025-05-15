package com.biblioteca.dao;

import com.biblioteca.core.ResetPasswordRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResetPasswordDAO {
    private static final Logger logger = LogManager.getLogger(ResetPasswordDAO.class);

    public boolean carnetExiste(String carnet) {
        String sql = "SELECT 1 FROM usuarios WHERE carnet = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, carnet);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de carnet: {}", carnet, e);
            return false;
        }
    }

    public boolean insertarSolicitud(String rol, String carnet, String nuevaContrasena) {
        if (!carnetExiste(carnet)) {
            logger.warn("insertarSolicitud falló: carnet {} no existe", carnet);
            return false;
        }

        String sql = "INSERT INTO solicitudes_reset (rol, carnet, nueva_contrasena) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rol);
            ps.setString(2, carnet);
            ps.setString(3, nuevaContrasena);
            int filas = ps.executeUpdate();
            logger.info("insertarSolicitud: rol={}, carnet={} → {} filas insertadas", rol, carnet, filas);
            return filas > 0;
        } catch (SQLException e) {
            logger.error("Error en insertarSolicitud rol={}, carnet={}", rol, carnet, e);
            return false;
        }
    }

    public List<ResetPasswordRequest> getTodasSolicitudes() {
        List<ResetPasswordRequest> lista = new ArrayList<>();
        String sql = "SELECT id, rol, carnet, nueva_contrasena, fecha_solicitud FROM solicitudes_reset";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ResetPasswordRequest req = new ResetPasswordRequest(
                    rs.getInt("id"),
                    rs.getString("rol"),
                    rs.getString("carnet"),
                    rs.getString("nueva_contrasena"),
                    rs.getTimestamp("fecha_solicitud")
                );
                lista.add(req);
            }
            logger.info("getTodasSolicitudes: {} solicitudes encontradas", lista.size());
        } catch (SQLException e) {
            logger.error("Error en getTodasSolicitudes", e);
        }
        return lista;
    }

    public boolean aplicarCambioContrasena(int solicitudId) {
        String sql = "UPDATE usuarios u " +
                     "JOIN solicitudes_reset s ON u.carnet = s.carnet " +
                     "SET u.password = s.nueva_contrasena " +
                     "WHERE s.id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, solicitudId);
            int filas = ps.executeUpdate();
            logger.info("aplicarCambioContrasena id={}: {} filas afectadas", solicitudId, filas);
            return filas > 0;
        } catch (SQLException e) {
            logger.error("Error en aplicarCambioContrasena id={}", solicitudId, e);
            return false;
        }
    }

    public boolean eliminarSolicitud(int solicitudId) {
        String sql = "DELETE FROM solicitudes_reset WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, solicitudId);
            int filas = ps.executeUpdate();
            logger.info("eliminarSolicitud id={}: {} filas eliminadas", solicitudId, filas);
            return filas > 0;
        } catch (SQLException e) {
            logger.error("Error en eliminarSolicitud id={}", solicitudId, e);
            return false;
        }
    }
}