package com.biblioteca.services;

import com.biblioteca.dao.Conexion;
import com.biblioteca.ui.GestionUsuariosWindow;
import com.biblioteca.ui.GestionMaterialesWindow;
import com.biblioteca.ui.ResetPasswordRequestsWindow;
import com.biblioteca.ui.HistorialGlobalPrestamosWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminService extends JFrame {
    private static final Logger logger = LogManager.getLogger(AdminService.class);
    private final String adminNombre;

    public AdminService(String adminNombre) {
        this.adminNombre = adminNombre;
        setTitle("Panel del Administrador - Bienvenido: " + adminNombre);
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        logger.info("Administrador con nombre {} ha iniciado sesión.", adminNombre);
        registrarAccesoDB();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("☰ Opciones");

        JMenuItem gestionarUsuarios = new JMenuItem("Gestionar usuarios");
        gestionarUsuarios.addActionListener(e -> new GestionUsuariosWindow());

        JMenuItem gestionarLibros = new JMenuItem("Gestionar libros");
        gestionarLibros.addActionListener(e -> new GestionMaterialesWindow());

        JMenuItem verSolicitudes = new JMenuItem("Ver solicitudes de restablecimiento");
        verSolicitudes.addActionListener(e -> new ResetPasswordRequestsWindow());

        JMenuItem verHistorial = new JMenuItem("Historial de préstamos");
        verHistorial.addActionListener(e -> new HistorialGlobalPrestamosWindow());

        JMenuItem cerrar = new JMenuItem("Cerrar Sesión y Salir");
        cerrar.setIcon(new ImageIcon(getClass().getResource("/images/exit_icon.png")));
        cerrar.setFont(new Font(cerrar.getFont().getName(), Font.BOLD, cerrar.getFont().getSize()));
        cerrar.setForeground(Color.RED);
        cerrar.addActionListener(this::onCerrarSesion);

        menu.add(gestionarUsuarios);
        menu.add(gestionarLibros);
        menu.add(verSolicitudes);
        menu.add(verHistorial);
        menu.addSeparator();
        menu.add(cerrar);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel center = new JPanel(new BorderLayout());
        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/images/logo.png")));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        center.add(logo, BorderLayout.CENTER);
        add(center);
    }

    private void onCerrarSesion(ActionEvent e) {
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Deseas cerrar sesión y salir?", "Confirmar salida",
            JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            logger.info("Administrador con nombre {} cerró sesión.", adminNombre);
            dispose();
            System.exit(0);
        }
    }

    private void registrarAccesoDB() {
        String sql = "INSERT INTO auditoria_usuarios (carnet, rol, accion) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, adminNombre);
            ps.setString(2, "admin");
            ps.setString(3, "inicio_sesion");
            ps.executeUpdate();
            logger.info("Registro de inicio de sesión guardado en DB para admin {}.", adminNombre);
        } catch (SQLException ex) {
            logger.error("Error al registrar acceso DB para admin {}: {}", adminNombre, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminService("admin"));
    }
}