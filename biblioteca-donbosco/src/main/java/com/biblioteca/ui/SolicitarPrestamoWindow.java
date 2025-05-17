package com.biblioteca.ui;

import com.biblioteca.core.Libro;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SolicitarPrestamoWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(SolicitarPrestamoWindow.class);

    private JTable librosTable;
    private DefaultTableModel tableModel;
    private JButton solicitarButton;

    private LibroDAO libroDAO = new LibroDAO();
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    private String carnetUsuario;

    public SolicitarPrestamoWindow(String carnet) {
        this.carnetUsuario = carnet;

        setTitle("Solicitar Préstamo de Libro");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        setVisible(true);

        logger.info("Ventana 'SolicitarPrestamoWindow' abierta para el usuario con carnet: {}", carnetUsuario);
    }

    private void initComponents() {
        String[] columnas = {"ID", "Título", "Autor", "Categoría"};
        tableModel = new DefaultTableModel(columnas, 0);
        librosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(librosTable);

        add(scrollPane, BorderLayout.CENTER);

        solicitarButton = new JButton("Solicitar Préstamo");
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonPanel.add(solicitarButton);

        add(botonPanel, BorderLayout.SOUTH);

        solicitarButton.addActionListener(this::solicitarPrestamo);

        cargarLibrosDisponibles();
    }

    private void cargarLibrosDisponibles() {
        tableModel.setRowCount(0);
        try {
            List<Libro> disponibles = libroDAO.getDisponibles();
            logger.info("Se cargaron {} libros disponibles.", disponibles.size());

            for (Libro libro : disponibles) {
                tableModel.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getCategoria()
                });
            }
        } catch (Exception ex) {
            logger.error("Error al cargar los libros disponibles.", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar libros disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solicitarPrestamo(ActionEvent e) {
        int filaSeleccionada = librosTable.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro para solicitar el préstamo.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            logger.warn("Intento de solicitud sin seleccionar libro por parte del usuario: {}", carnetUsuario);
            return;
        }

        int libroId = (int) tableModel.getValueAt(filaSeleccionada, 0);
        String tituloLibro = (String) tableModel.getValueAt(filaSeleccionada, 1);

        // Verificar préstamo activo idéntico
        boolean yaPrestado = prestamoDAO.tienePrestamoActivo(carnetUsuario, libroId);
        if (yaPrestado) {
            JOptionPane.showMessageDialog(this,
                    "Ya tienes un préstamo activo de este libro (" + tituloLibro + ").",
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            logger.warn("Usuario {} intentó solicitar de nuevo el libro ID {}", carnetUsuario, libroId);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas solicitar el préstamo del libro \"" + tituloLibro + "\"?",
                "Confirmar solicitud",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            logger.info("El usuario {} solicitó préstamo del libro ID: {}, Título: {}", carnetUsuario, libroId, tituloLibro);
            try {
                boolean exito = prestamoDAO.crearPrestamo(carnetUsuario, libroId);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Préstamo realizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Préstamo exitoso para el usuario {} y libro ID {}", carnetUsuario, libroId);
                    cargarLibrosDisponibles();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo realizar el préstamo. Verifica disponibilidad o errores en la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    logger.warn("Fallo al crear préstamo para el usuario {} y libro ID {}", carnetUsuario, libroId);
                }
            } catch (Exception ex) {
                logger.error("Error al procesar el préstamo para el usuario {}", carnetUsuario, ex);
                JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado. Consulta los logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
