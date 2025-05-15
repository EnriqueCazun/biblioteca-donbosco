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

public class DevolverLibroWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(DevolverLibroWindow.class);

    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private JButton devolverButton;

    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private String carnetUsuario;

    public DevolverLibroWindow(String carnet) {
        this.carnetUsuario = carnet;

        setTitle("Devolver Libro — Usuario: " + carnetUsuario);
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        setVisible(true);

        logger.info("Se abrió DevolverLibroWindow para el usuario {}", carnetUsuario);
    }

    private void initComponents() {
        String[] columnas = {"ID Préstamo", "Título del Libro", "Fecha de Préstamo"};
        tableModel = new DefaultTableModel(columnas, 0);
        prestamosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        add(scrollPane, BorderLayout.CENTER);

        devolverButton = new JButton("Devolver Libro");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(devolverButton);
        add(panelBoton, BorderLayout.SOUTH);
        devolverButton.addActionListener(this::devolverLibro);
        cargarPrestamosActivos();
    }

    private void cargarPrestamosActivos() {
        tableModel.setRowCount(0);
        try {
            List<Prestamo> prestamos = prestamoDAO.getPrestamosActivosPorUsuario(carnetUsuario);
            logger.info("Cargando {} préstamos activos para {}", prestamos.size(), carnetUsuario);

            for (Prestamo p : prestamos) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getLibroTitulo(),
                    p.getFechaPrestamo()
                });
            }

            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No tienes préstamos activos.",
                    "Sin préstamos",
                    JOptionPane.INFORMATION_MESSAGE);
                logger.info("El usuario {} no tiene préstamos activos.", carnetUsuario);
            }
        } catch (Exception ex) {
            logger.error("Error al cargar préstamos activos para {}", carnetUsuario, ex);
            JOptionPane.showMessageDialog(this,
                "Error al cargar tus préstamos. Revisa los logs.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverLibro(ActionEvent e) {
        int fila = prestamosTable.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un préstamo para devolver.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            logger.warn("Usuario {} intentó devolver sin seleccionar préstamo", carnetUsuario);
            return;
        }

        int prestamoId = (int) tableModel.getValueAt(fila, 0);
        String titulo = (String) tableModel.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Confirma devolución de \"" + titulo + "\"?",
            "Confirmar devolución",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            logger.info("Usuario {} confirma devolución del préstamo {}", carnetUsuario, prestamoId);
            try {
                boolean ok = prestamoDAO.registrarDevolucion(prestamoId);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                        "Libro devuelto correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Devolución exitosa: préstamo {}", prestamoId);
                    cargarPrestamosActivos();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo procesar la devolución.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    logger.warn("Fallo al devolver préstamo {}", prestamoId);
                }
            } catch (Exception ex) {
                logger.error("Error al procesar devolución de préstamo {}", prestamoId, ex);
                JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado. Consulta los logs.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new DevolverLibroWindow("20250101").setVisible(true)
        );
    }
}