package com.biblioteca.ui;

import com.biblioteca.core.ResetPasswordRequest;
import com.biblioteca.dao.ResetPasswordDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ResetPasswordRequestsWindow extends JFrame {
    private static final Logger logger = LogManager.getLogger(ResetPasswordRequestsWindow.class);

    private JTable solicitudesTable;
    private DefaultTableModel tableModel;
    private JButton aceptarButton;
    private JButton rechazarButton;

    private ResetPasswordDAO resetPasswordDAO = new ResetPasswordDAO();

    public ResetPasswordRequestsWindow() {
        setTitle("Solicitudes de Restablecimiento de Contraseña");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarSolicitudes();
        setVisible(true);

        logger.info("Ventana de solicitudes de restablecimiento abierta");
    }

    private void initComponents() {
        String[] columnas = {"ID", "Rol", "Carnet", "Fecha Solicitud"};
        tableModel = new DefaultTableModel(columnas, 0);
        solicitudesTable = new JTable(tableModel);
        add(new JScrollPane(solicitudesTable), BorderLayout.CENTER);

        aceptarButton = new JButton("Aceptar");
        rechazarButton = new JButton("Rechazar");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(aceptarButton);
        btnPanel.add(rechazarButton);
        add(btnPanel, BorderLayout.SOUTH);

        aceptarButton.addActionListener(this::aceptarSolicitud);
        rechazarButton.addActionListener(this::rechazarSolicitud);
    }

    private void cargarSolicitudes() {
        tableModel.setRowCount(0);
        try {
            List<ResetPasswordRequest> list = resetPasswordDAO.getTodasSolicitudes();
            logger.info("Cargando {} solicitudes", list.size());
            for (ResetPasswordRequest r : list) {
                tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getRol(),
                    r.getCarnet(),
                    r.getFechaSolicitud()
                });
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay solicitudes registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                logger.info("No hay solicitudes registradas");
            }
        } catch (Exception ex) {
            logger.error("Error al cargar solicitudes", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar solicitudes. Ver logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aceptarSolicitud(ActionEvent e) {
        int row = solicitudesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una solicitud.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            logger.warn("Aceptar sin selección");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Aceptar y aplicar el cambio de contraseña para la solicitud ID " + id + "?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            try {
                boolean exito = resetPasswordDAO.aplicarCambioContrasena(id);
                if (exito) {
                    resetPasswordDAO.eliminarSolicitud(id);
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada y solicitud eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Solicitud {} aceptada y eliminada", id);
                    cargarSolicitudes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.warn("Fallo al aceptar solicitud {}", id);
                }
            } catch (Exception ex) {
                logger.error("Error al aceptar solicitud " + id, ex);
                JOptionPane.showMessageDialog(this, "Error inesperado. Ver logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rechazarSolicitud(ActionEvent e) {
        int row = solicitudesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una solicitud.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            logger.warn("Rechazar sin selección");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Rechazar y eliminar la solicitud ID " + id + "?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            try {
                if (resetPasswordDAO.eliminarSolicitud(id)) {
                    JOptionPane.showMessageDialog(this, "Solicitud eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Solicitud {} rechazada (eliminada)", id);
                    cargarSolicitudes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.warn("Fallo al eliminar solicitud {}", id);
                }
            } catch (Exception ex) {
                logger.error("Error al rechazar solicitud " + id, ex);
                JOptionPane.showMessageDialog(this, "Error inesperado. Ver logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ResetPasswordRequestsWindow::new);
    }
}