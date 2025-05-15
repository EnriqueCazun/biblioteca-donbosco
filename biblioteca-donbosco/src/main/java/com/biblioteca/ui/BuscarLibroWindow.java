package com.biblioteca.ui;

import com.biblioteca.core.Libro;
import com.biblioteca.dao.LibroDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BuscarLibroWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(BuscarLibroWindow.class);

    private JComboBox<String> criterioCombo;
    private JTextField campoBusqueda;
    private JButton buscarButton;
    private JTable resultadosTable;
    private DefaultTableModel tableModel;

    private LibroDAO libroDAO = new LibroDAO();

    public BuscarLibroWindow() {
        setTitle("Buscar Libros");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        actualizarTabla(libroDAO.getAllLibros());
        setVisible(true);

        logger.info("BuscarLibroWindow inicializado correctamente.");
    }

    private void initComponents() {
        JPanel searchPanel = new JPanel(new FlowLayout());

        criterioCombo = new JComboBox<>(new String[]{"Título", "Autor", "Categoría"});
        campoBusqueda = new JTextField(30);
        buscarButton = new JButton("Buscar");

        searchPanel.add(new JLabel("Buscar por:"));
        searchPanel.add(criterioCombo);
        searchPanel.add(campoBusqueda);
        searchPanel.add(buscarButton);

        add(searchPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Título", "Autor", "Categoría", "Disponible"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultadosTable = new JTable(tableModel);
        resultadosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(resultadosTable), BorderLayout.CENTER);

        buscarButton.addActionListener(this::realizarBusqueda);
    }

    private void realizarBusqueda(ActionEvent e) {
        String criterio = (String) criterioCombo.getSelectedItem();
        String valor = campoBusqueda.getText().trim();

        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un valor de búsqueda.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            logger.warn("Búsqueda cancelada: el campo de búsqueda está vacío.");
            return;
        }

        logger.info("Realizando búsqueda por '{}' con valor '{}'", criterio, valor);

        List<Libro> resultados;
        switch (criterio) {
            case "Título":
                resultados = libroDAO.buscarPorTitulo(valor);
                break;
            case "Autor":
                resultados = libroDAO.buscarPorAutor(valor);
                break;
            case "Categoría":
                resultados = libroDAO.buscarPorCategoria(valor);
                break;
            default:
                resultados = libroDAO.getAllLibros();
                break;
        }

        actualizarTabla(resultados);
        logger.info("Se encontraron {} resultados para la búsqueda.", resultados.size());
    }

    private void actualizarTabla(List<Libro> libros) {
        tableModel.setRowCount(0);
        if (libros == null || libros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            logger.info("No se encontraron resultados en la búsqueda.");
            return;
        }

        for (Libro libro : libros) {
            tableModel.addRow(new Object[]{
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCategoria(),
                libro.isDisponible() ? "Sí" : "No"
            });
        }
    }
}