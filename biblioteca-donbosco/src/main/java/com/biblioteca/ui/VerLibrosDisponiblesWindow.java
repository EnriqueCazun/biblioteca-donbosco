package com.biblioteca.ui;

import com.biblioteca.core.Libro;
import com.biblioteca.dao.LibroDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerLibrosDisponiblesWindow extends JFrame {

    private JTable librosTable;
    private DefaultTableModel tableModel;

    private LibroDAO libroDAO = new LibroDAO();

    public VerLibrosDisponiblesWindow() {
        setTitle("Libros Disponibles");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        cargarLibrosDisponibles();
        setVisible(true);
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Libros disponibles en la biblioteca"));
        add(topPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Autor", "Categoría", "Tipo", "Fecha de Publicación", "Páginas"};

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        librosTable = new JTable(tableModel);
        librosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        librosTable.setRowSelectionAllowed(true);
        librosTable.setColumnSelectionAllowed(false);
        librosTable.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(librosTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarLibrosDisponibles() {
        tableModel.setRowCount(0);

        List<Libro> librosDisponibles = libroDAO.getDisponibles();
        if (librosDisponibles != null && !librosDisponibles.isEmpty()) {
            for (Libro libro : librosDisponibles) {
                tableModel.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getCategoria(),
                        libro.getTipo(),
                        libro.getFechaPublicacion(),
                        libro.getPaginas()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay libros disponibles en este momento.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}