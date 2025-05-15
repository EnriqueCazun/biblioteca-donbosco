package com.biblioteca.services;

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

public class AlumnoService extends JFrame {
    private static final Logger logger = LogManager.getLogger(AlumnoService.class);

    public AlumnoService(String carnet) {
        setTitle("Biblioteca Don Bosco - Panel del Alumno - Carnet: " + carnet);
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        logger.info("Alumno con carnet {} ha iniciado sesión.", carnet);
        initComponents(carnet);
        setVisible(true);
    }

    private void initComponents(String carnet) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("☰ Opciones");

        JMenuItem buscarLibros = new JMenuItem("Buscar libros");
        buscarLibros.addActionListener(e -> new BuscarLibroWindow());

        JMenuItem verDisponibles = new JMenuItem("Ver libros disponibles");
        verDisponibles.addActionListener(e -> new VerLibrosDisponiblesWindow());

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
        cerrar.addActionListener((ActionEvent e) -> {
            int resp = JOptionPane.showConfirmDialog(this,
                    "¿Deseas cerrar sesión y salir?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                logger.info("Alumno con carnet {} cerró sesión.", carnet);
                dispose();
                System.exit(0);
            }
        });

        menu.add(buscarLibros);
        menu.add(verDisponibles);
        menu.add(misPrestamos);
        menu.add(solicitar);
        menu.add(devolver);
        menu.add(historial);
        menu.addSeparator();
        menu.add(cerrar);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel(new ImageIcon(getClass().getResource("/images/logo.png"))), BorderLayout.CENTER);
        add(center);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlumnoService("20250101"));
    }
}