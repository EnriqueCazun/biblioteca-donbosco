package com.biblioteca.ui;

import com.biblioteca.core.Prestamo;
import com.biblioteca.dao.PrestamoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MisPrestamosWindow extends JFrame {
    private static final Logger logger = LogManager.getLogger(MisPrestamosWindow.class);

    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private String carnetUsuario;
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    public MisPrestamosWindow(String carnet) {
        this.carnetUsuario = carnet;
        setTitle("Mis Préstamos Actuales — " + carnetUsuario);
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarPrestamosActivos();
        setVisible(true);

        logger.info("Ventana 'MisPrestamosWindow' iniciada para {}", carnetUsuario);
    }

    private void initComponents() {
        String[] cols = {"ID Préstamo", "Título del Libro", "Fecha de Préstamo", "Estado"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        prestamosTable = new JTable(tableModel);
        prestamosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prestamosTable.setRowSelectionAllowed(true);
        prestamosTable.setColumnSelectionAllowed(false);
        prestamosTable.setDefaultEditor(Object.class, null);

        add(new JScrollPane(prestamosTable), BorderLayout.CENTER);
    }

    private void cargarPrestamosActivos() {
        tableModel.setRowCount(0);
        try {
            List<Prestamo> lista = prestamoDAO.getPrestamosActivosPorUsuario(carnetUsuario);
            logger.info("Se cargaron {} préstamos activos para {}", lista.size(), carnetUsuario);

            for (Prestamo p : lista) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getLibroTitulo(),
                    p.getFechaPrestamo(),
                    p.getEstado()
                });
            }

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No tienes préstamos activos.",
                    "Sin préstamos",
                    JOptionPane.INFORMATION_MESSAGE);
                logger.info("Usuario {} no tiene préstamos activos", carnetUsuario);
            }
        } catch (Exception ex) {
            logger.error("Error al cargar préstamos activos para {}", carnetUsuario, ex);
            JOptionPane.showMessageDialog(this,
                "Error al cargar tus préstamos. Consulta los logs.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MisPrestamosWindow("20250101"));
    }
}