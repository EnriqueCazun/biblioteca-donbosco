package com.biblioteca.ui;

import com.biblioteca.core.Prestamo;
import com.biblioteca.dao.PrestamoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HistorialGlobalPrestamosWindow extends JFrame {
    private static final Logger logger = LogManager.getLogger(HistorialGlobalPrestamosWindow.class);

    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private JButton cargarButton;

    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    public HistorialGlobalPrestamosWindow() {
        setTitle("Historial Global de Préstamos");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarHistorial();
        setVisible(true);

        logger.info("Ventana HistorialGlobalPrestamosWindow iniciada");
    }

    private void initComponents() {
    	
        String[] columnas = {"ID Préstamo", "Usuario ID", "Título del Libro", "Fecha Préstamo", "Fecha Devolución"};
        tableModel = new DefaultTableModel(columnas, 0);
        prestamosTable = new JTable(tableModel);
        add(new JScrollPane(prestamosTable), BorderLayout.CENTER);

        cargarButton = new JButton("Recargar Historial");
        cargarButton.addActionListener(this::onCargarClicked);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(cargarButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void onCargarClicked(ActionEvent e) {
        cargarHistorial();
    }

    private void cargarHistorial() {
        tableModel.setRowCount(0);
        try {
            List<Prestamo> prestamos = prestamoDAO.getTodosLosPrestamos();
            logger.info("Cargando historial global: {} registros", prestamos.size());

            for (Prestamo p : prestamos) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getUsuarioId(),
                    p.getLibroTitulo(),
                    p.getFechaPrestamo(),
                    p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "Pendiente"
                });
            }

            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay registros de préstamos.",
                    "Sin datos",
                    JOptionPane.INFORMATION_MESSAGE);
                logger.info("No se encontraron préstamos en el sistema");
            }
        } catch (Exception ex) {
            logger.error("Error al cargar historial global de préstamos", ex);
            JOptionPane.showMessageDialog(this,
                "Error al cargar historial. Consulta los logs.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HistorialGlobalPrestamosWindow::new);
    }
}