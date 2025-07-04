package com.biblioteca.dao;

import com.biblioteca.core.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class);

    public List<Usuario> getAllUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, carnet, nombre, apellido, password, rol FROM usuarios";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
            logger.info("getAllUsuarios: {} usuarios obtenidos", lista.size());
        } catch (SQLException e) {
            logger.error("Error en getAllUsuarios", e);
        }
        return lista;
    }

    public boolean agregarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (carnet, nombre, apellido, password, rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getCarnet());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getRol());

            int filas = ps.executeUpdate();
            logger.info("agregarUsuario {}: {} filas insertadas", usuario.getCarnet(), filas);
            return filas > 0;
        } catch (SQLException e) {
            logger.error("Error en agregarUsuario {}", usuario, e);
            return false;
        }
    }

    public boolean modificarUsuario(String oldCarnet, Usuario usuario) {
        String sql = "UPDATE usuarios SET carnet = ?, nombre = ?, apellido = ?, password = ?, rol = ? WHERE carnet = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getCarnet());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getRol());
            ps.setString(6, oldCarnet);

            int filas = ps.executeUpdate();
            logger.info("modificarUsuario {}->{}: {} filas actualizadas", oldCarnet, usuario.getCarnet(), filas);
            return filas > 0;
        } catch (SQLException e) {
            logger.error("Error en modificarUsuario {}->{}", oldCarnet, usuario, e);
            return false;
        }
    }

    /**
     * Elimina primero los préstamos asociados al usuario y luego al propio usuario.
     */
    public boolean eliminarUsuario(String carnet) {
        String sqlDeletePrestamos = 
            "DELETE p FROM prestamos p " +
            "JOIN usuarios u ON p.usuario_id = u.id " +
            "WHERE u.carnet = ?";
        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE carnet = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement psPrestamos = con.prepareStatement(sqlDeletePrestamos);
             PreparedStatement psUsuario = con.prepareStatement(sqlDeleteUsuario)) {

            // 1) Borrar préstamos del usuario
            psPrestamos.setString(1, carnet);
            int presRows = psPrestamos.executeUpdate();
            logger.info("eliminarUsuario {}: {} préstamos eliminados", carnet, presRows);

            // 2) Borrar el usuario
            psUsuario.setString(1, carnet);
            int usrRows = psUsuario.executeUpdate();
            logger.info("eliminarUsuario {}: {} usuarios eliminados", carnet, usrRows);

            return usrRows > 0;
        } catch (SQLException e) {
            logger.error("Error en eliminarUsuario {}", carnet, e);
            return false;
        }
    }

    // Método de utilidad: chequea si tiene préstamos (sigue disponible si se desea)
    public boolean tienePrestamos(String carnet) {
        String sqlCount = "SELECT COUNT(*) FROM prestamos p " +
                          "JOIN usuarios u ON p.usuario_id = u.id " +
                          "WHERE u.carnet = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCount)) {
            ps.setString(1, carnet);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error en tienePrestamos {}", carnet, e);
            return true;
        }
    }

    public Usuario autenticarUsuario(String carnet, String password) {
        String sql = "SELECT id, carnet, nombre, apellido, password, rol FROM usuarios WHERE carnet = ? AND password = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, carnet);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = mapearUsuario(rs);
                    logger.info("autenticarUsuario: login exitoso para carnet={}", carnet);
                    return u;
                }
            }
            logger.warn("autenticarUsuario: credenciales inválidas para carnet={}", carnet);
        } catch (SQLException e) {
            logger.error("Error en autenticarUsuario carnet={}", carnet, e);
        }
        return null;
    }

    public Usuario getUsuarioPorCarnet(String carnet) {
        String sql = "SELECT id, carnet, nombre, apellido, password, rol FROM usuarios WHERE carnet = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, carnet);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = mapearUsuario(rs);
                    logger.info("getUsuarioPorCarnet: encontrado {}", carnet);
                    return u;
                }
            }
        } catch (SQLException e) {
            logger.error("Error en getUsuarioPorCarnet {}", carnet, e);
        }
        return null;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("carnet"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("password"),
            rs.getString("rol")
        );
    }
}
