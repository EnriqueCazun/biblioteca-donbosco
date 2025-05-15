package com.biblioteca.ui;

import com.biblioteca.core.*;
import com.biblioteca.dao.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestionMaterialesWindow extends JFrame {

    private JTable materialesTable;
    private DefaultTableModel tableModel;

    private JTextField idField, tituloField, ubicacionField, cantidadField, prestadosField,
            autorField, fechaPublicacionField, paginasMinutosField;
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> generoCategoriaField;
    private JButton agregarButton, eliminarButton, generarIdButton;

    private final LibroDAO libroDAO = new LibroDAO();
    private final RevistaDAO revistaDAO = new RevistaDAO();
    private final MaterialAudiovisualDAO audiovisualDAO = new MaterialAudiovisualDAO();
    private final ManuscritoDAO manuscritoDAO = new ManuscritoDAO();
    private final MaterialReferenciaDAO referenceDAO = new MaterialReferenciaDAO();

    public GestionMaterialesWindow() {
        setTitle("Gestión de Materiales");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Tipo:"), gbc);
        tipoComboBox = new JComboBox<>(new String[]{"Libro", "Revista", "Audiovisual", "Manuscrito", "Referencia"});
        gbc.gridx = 1;
        formPanel.add(tipoComboBox, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("ID:"), gbc);
        idField = new JTextField(10);
        gbc.gridx = 3;
        formPanel.add(idField, gbc);

        generarIdButton = new JButton("Generar ID");
        generarIdButton.addActionListener(this::generarId);
        gbc.gridx = 4;
        formPanel.add(generarIdButton, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        formPanel.add(new JLabel("Título:"), gbc);
        tituloField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(tituloField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Autor:"), gbc);
        autorField = new JTextField(15);
        gbc.gridx = 3;
        formPanel.add(autorField, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        formPanel.add(new JLabel("Fecha Publicación:"), gbc);
        fechaPublicacionField = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(fechaPublicacionField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Ubicación Física:"), gbc);
        ubicacionField = new JTextField(5);
        gbc.gridx = 3;
        formPanel.add(ubicacionField, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        formPanel.add(new JLabel("Cantidad Total:"), gbc);
        cantidadField = new JTextField(5);
        gbc.gridx = 1;
        formPanel.add(cantidadField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Prestados:"), gbc);
        prestadosField = new JTextField(5);
        gbc.gridx = 3;
        formPanel.add(prestadosField, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        formPanel.add(new JLabel("Páginas/Minutos:"), gbc);
        paginasMinutosField = new JTextField(5);
        gbc.gridx = 1;
        formPanel.add(paginasMinutosField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Género/Categoría:"), gbc);
        generoCategoriaField = new JComboBox<>(new String[]{
            "Ciencia Ficción", "Historia", "Biografía", "Misterio", "Romance",
            "Fantasía", "Tecnología", "Filosofía", "Psicología", "Arte"
        });
        gbc.gridx = 3;
        formPanel.add(generoCategoriaField, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 1;
        agregarButton = new JButton("Agregar");
        agregarButton.addActionListener(this::agregarMaterial);
        formPanel.add(agregarButton, gbc);

        gbc.gridx = 2;
        eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(this::eliminarMaterial);
        formPanel.add(eliminarButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
                "Tipo", "ID", "Título", "Autor", "Ubicación Física", "Total", "Prestados", "Páginas/Minutos", "Género/Categoría"
        }, 0);
        materialesTable = new JTable(tableModel);
        add(new JScrollPane(materialesTable), BorderLayout.CENTER);

        cargarMateriales();
    }

    private void cargarMateriales() {
        tableModel.setRowCount(0);
        List<RecursoBiblioteca> materiales = new ArrayList<>();
        materiales.addAll(libroDAO.getAllLibros());
        materiales.addAll(revistaDAO.getAll());
        materiales.addAll(audiovisualDAO.getAll());
        materiales.addAll(manuscritoDAO.getAll());
        materiales.addAll(referenceDAO.getAll());

        for (RecursoBiblioteca material : materiales) {
            String autor = "N/A";
            String paginasDuracion = "N/A";
            String generoCat = "N/A";

            if (material instanceof Libro) {
                Libro libro = (Libro) material;
                autor = libro.getAutor();
                paginasDuracion = String.valueOf(libro.getPaginas());
                generoCat = libro.getCategoria();
            } else if (material instanceof Revista) {
                Revista revista = (Revista) material;
                autor = revista.getAutor();
            } else if (material instanceof MaterialAudiovisual) {
                MaterialAudiovisual audiovisual = (MaterialAudiovisual) material;
                autor = audiovisual.getAutor();
                paginasDuracion = String.valueOf(audiovisual.getDuracion());
                generoCat = audiovisual.getCategoria();
            } else if (material instanceof Manuscrito) {
                Manuscrito manuscrito = (Manuscrito) material;
                autor = manuscrito.getAutor();
                generoCat = manuscrito.getCategoria();
            } else if (material instanceof MaterialReferencia) {
                MaterialReferencia ref = (MaterialReferencia) material;
                autor = "N/A";
                generoCat = ref.getTipoMaterial();
            }

            tableModel.addRow(new Object[]{
                    material.getClass().getSimpleName(),
                    material.getId(),
                    material.getTitulo(),
                    autor,
                    material.getUbicacionFisica(),
                    material.getCantidadTotal(),
                    material.getCantidadPrestada(),
                    paginasDuracion,
                    generoCat
            });
        }
    }

    private void agregarMaterial(ActionEvent e) {
        try {
            String tipo = (String) tipoComboBox.getSelectedItem();
            int id = Integer.parseInt(idField.getText().trim());
            String titulo = tituloField.getText().trim();
            String autor = autorField.getText().trim();
            String fecha = fechaPublicacionField.getText().trim();
            String ubicacion = ubicacionField.getText().trim();
            int total = Integer.parseInt(cantidadField.getText().trim());
            int prestados = Integer.parseInt(prestadosField.getText().trim());
            int paginasMin = Integer.parseInt(paginasMinutosField.getText().trim());
            String generoCat = (String) generoCategoriaField.getSelectedItem();

            boolean exito = false;
            switch (tipo) {
                case "Libro":
                    exito = libroDAO.agregar(new Libro(id, titulo, ubicacion, total, prestados, autor, fecha, paginasMin, generoCat, tipo));
                    break;
                case "Revista":
                    exito = revistaDAO.agregar(new Revista(id, titulo, ubicacion, total, prestados, autor, fecha));
                    break;
                case "Audiovisual":
                    exito = audiovisualDAO.agregar(new MaterialAudiovisual(id, titulo, autor, ubicacion, total, prestados, fecha, generoCat, paginasMin));
                    break;
                case "Manuscrito":
                    exito = manuscritoDAO.agregar(new Manuscrito(id, titulo, autor, generoCat, new Date()));
                    break;
                case "Referencia":
                    exito = referenceDAO.agregar(new MaterialReferencia(id, titulo, ubicacion, total, prestados, autor, true));
                    break;
            }

            JOptionPane.showMessageDialog(this, exito ? tipo + " agregado correctamente." : "Error al agregar.");
            if (exito) cargarMateriales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifica los campos numéricos.");
        }
    }

    private void eliminarMaterial(ActionEvent e) {
        int fila = materialesTable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un material para eliminar.");
            return;
        }

        String tipo = (String) tableModel.getValueAt(fila, 0);
        int id = (int) tableModel.getValueAt(fila, 1);

        boolean exito = false;
        switch (tipo) {
            case "Libro": exito = libroDAO.eliminarLibro(id); break;
            case "Revista": exito = revistaDAO.eliminar(id); break;
            case "Audiovisual": exito = audiovisualDAO.eliminar(id); break;
            case "Manuscrito": exito = manuscritoDAO.eliminar(id); break;
            case "Referencia": exito = referenceDAO.eliminar(id); break;
        }

        JOptionPane.showMessageDialog(this, exito ? tipo + " eliminado correctamente." : "Error al eliminar.");
        if (exito) cargarMateriales();
    }

    private void generarId(ActionEvent e) {
        String base = tituloField.getText().trim() + ubicacionField.getText().trim();
        idField.setText(String.valueOf(Math.abs(base.hashCode())));
    }
}