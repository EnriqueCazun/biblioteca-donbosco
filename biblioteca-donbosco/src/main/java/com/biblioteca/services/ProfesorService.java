package com.biblioteca.services;

import com.biblioteca.dao.Conexion;
import com.biblioteca.ui.BuscarLibroWindow;
import com.biblioteca.ui.VerLibrosDisponiblesWindow;
import com.biblioteca.ui.MisPrestamosWindow;
import com.biblioteca.ui.SolicitarPrestamoWindow;
import com.biblioteca.ui.DevolverLibroWindow;
import com.biblioteca.ui.HistorialPrestamosWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfesorService extends JFrame {
    private static final Logger logger = LogManager.getLogger(ProfesorService.class);
    private final String carnet;

    public ProfesorService(String carnet) {
        this.carnet = carnet;
        setTitle("Biblioteca Don Bosco - Panel del Profesor - Carnet: " + carnet);
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        logger.info("Profesor con carnet {} ha iniciado sesión.", carnet);
        registrarAccesoDB();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("☰ Opciones");

        JMenuItem buscar = new JMenuItem("Buscar libros");
        buscar.addActionListener(e -> new BuscarLibroWindow());

        JMenuItem disponibles = new JMenuItem("Ver libros disponibles");
        disponibles.addActionListener(e -> new VerLibrosDisponiblesWindow());

        JMenuItem misPrestamos = new JMenuItem("Ver mis préstamos actuales");
        misPrestamos.addActionListener(e -> new MisPrestamosWindow(carnet));

        JMenuItem solicitar = new JMenuItem("Solicitar nuevo préstamo");
        solicitar.addActionListener(e -> new SolicitarPrestamoWindow(carnet));

        JMenuItem devolver = new JMenuItem("Devolver libro");
        devolver.addActionListener(e -> new DevolverLibroWindow(carnet));

        JMenuItem historial = new JMenuItem("Historial de préstamos");
        historial.addActionListener(e -> new HistorialPrestamosWindow(carnet));

        JMenuItem cerrar = new JMenuItem("Cerrar Sesión y Salir");
        cerrar.setIcon(new ImageIcon(getClass().getResource("/images/exit_icon.png")));
        cerrar.setFont(new Font(cerrar.getFont().getName(), Font.BOLD, cerrar.getFont().getSize()));
        cerrar.setForeground(Color.RED);
        cerrar.addActionListener(this::onCerrarSesion);

        menu.add(buscar);
        menu.add(disponibles);
        menu.add(misPrestamos);
        menu.add(solicitar);
        menu.add(devolver);
        menu.add(historial);
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
            logger.info("Profesor con carnet {} cerró sesión.", carnet);
            dispose();
            System.exit(0);
        }
    }

    private void registrarAccesoDB() {
        String sql = "INSERT INTO auditoria_usuarios (carnet, rol, accion) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setString(2, "profesor");
            ps.setString(3, "inicio_sesion");
            ps.executeUpdate();
            logger.info("Registro de inicio de sesión guardado en DB para profesor {}.", carnet);
        } catch (SQLException ex) {
            logger.error("Error al registrar acceso DB para profesor {}: {}", carnet, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProfesorService("P20250101"));
    }
}