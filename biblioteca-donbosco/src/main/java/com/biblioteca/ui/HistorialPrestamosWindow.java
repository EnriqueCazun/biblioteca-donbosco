package com.biblioteca.ui;

import com.biblioteca.core.Prestamo;
import com.biblioteca.dao.PrestamoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialPrestamosWindow extends JFrame {

    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private String carnetUsuario;

    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    public HistorialPrestamosWindow(String carnet) {
        this.carnetUsuario = carnet;

        setTitle("Historial de Préstamos");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        String[] columnas = {"ID Préstamo", "Título del Libro", "Fecha Préstamo", "Fecha Devolución"};
        tableModel = new DefaultTableModel(columnas, 0);
        prestamosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        add(scrollPane, BorderLayout.CENTER);

        cargarHistorialPrestamos();
    }

    private void cargarHistorialPrestamos() {
        tableModel.setRowCount(0);

        List<Prestamo> historialPrestamos = prestamoDAO.getHistorialPorUsuario(carnetUsuario);

        if (historialPrestamos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron préstamos previos.", "Sin historial", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Prestamo prestamo : historialPrestamos) {
            tableModel.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getLibroTitulo(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion() != null ? prestamo.getFechaDevolucion() : "Pendiente"
            });
        }
    }
}