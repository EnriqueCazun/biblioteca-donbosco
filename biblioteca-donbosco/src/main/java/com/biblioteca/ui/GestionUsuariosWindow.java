package com.biblioteca.ui;

import com.biblioteca.core.Usuario;
import com.biblioteca.dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

public class GestionUsuariosWindow extends JFrame {

    private JTable usuariosTable;
    private DefaultTableModel tableModel;
    private JTextField carnetField;
    private JTextField nombreField;
    private JPasswordField passwordField;
    private JComboBox<String> rolComboBox;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JButton generarCarnetButton;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private String originalCarnet;

    public GestionUsuariosWindow() {
        setTitle("Gestionar Usuarios");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new FlowLayout());

        inputPanel.add(new JLabel("Carnet:"));
        carnetField = new JTextField(10);
        inputPanel.add(carnetField);

        inputPanel.add(new JLabel("Nombre y Apellido:"));
        nombreField = new JTextField(20);
        inputPanel.add(nombreField);

        inputPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(10);
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Rol:"));
        rolComboBox = new JComboBox<>(new String[]{"Administrador", "Profesor", "Alumno"});
        inputPanel.add(rolComboBox);

        generarCarnetButton = new JButton("Generar Carnet");
        agregarButton = new JButton("Agregar Usuario");
        modificarButton = new JButton("Modificar Usuario");
        eliminarButton = new JButton("Eliminar Usuario");

        inputPanel.add(generarCarnetButton);
        inputPanel.add(agregarButton);
        inputPanel.add(modificarButton);
        inputPanel.add(eliminarButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnas = {"Carnet", "Nombre", "Rol"};
        tableModel = new DefaultTableModel(columnas, 0);
        usuariosTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        add(scrollPane, BorderLayout.CENTER);

        cargarUsuarios();

        agregarButton.addActionListener(this::agregarUsuario);
        modificarButton.addActionListener(this::modificarUsuario);
        eliminarButton.addActionListener(this::eliminarUsuario);
        generarCarnetButton.addActionListener(this::generarCarnet);

        usuariosTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = usuariosTable.getSelectedRow();
            if (selectedRow >= 0) {
                originalCarnet = (String) tableModel.getValueAt(selectedRow, 0);
                carnetField.setText(originalCarnet);
                nombreField.setText((String) tableModel.getValueAt(selectedRow, 1));
                rolComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
                passwordField.setText("");
            }
        });
    }

    private void cargarUsuarios() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.getAllUsuarios();
        for (Usuario usuario : usuarios) {
            tableModel.addRow(new Object[] {
                    usuario.getCarnet(),
                    usuario.getNombre() + " " + usuario.getApellido(),
                    usuario.getRol()
            });
        }
    }

    private void agregarUsuario(ActionEvent e) {
        String carnet = carnetField.getText().trim();
        String nombreCompleto = nombreField.getText().trim();
        String rol = (String) rolComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword()).trim();

        if (carnet.isEmpty() || nombreCompleto.isEmpty() || rol == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] partes = nombreCompleto.split(" ", 2);
        String nom = partes[0];
        String ape = partes.length > 1 ? partes[1] : "";

        Usuario nuevoUsuario = new Usuario(carnet, nom, ape, password, rol);
        boolean exito = usuarioDAO.agregarUsuario(nuevoUsuario);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el usuario. Intenta más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarUsuario(ActionEvent e) {
        if (originalCarnet == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoCarnet = carnetField.getText().trim();
        String nombreCompleto = nombreField.getText().trim();
        String rol = (String) rolComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword()).trim();

        if (nuevoCarnet.isEmpty() || nombreCompleto.isEmpty() || rol == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] partes = nombreCompleto.split(" ", 2);
        String nom = partes[0];
        String ape = partes.length > 1 ? partes[1] : "";

        Usuario usuarioModificado = new Usuario(nuevoCarnet, nom, ape, password, rol);
        boolean exito = usuarioDAO.modificarUsuario(originalCarnet, usuarioModificado);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el usuario. Intenta más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        originalCarnet = null;
    }

    private void eliminarUsuario(ActionEvent e) {
        int filaSeleccionada = usuariosTable.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String carnet = (String) tableModel.getValueAt(filaSeleccionada, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar al usuario con carnet: " + carnet + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = usuarioDAO.eliminarUsuario(carnet);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario. Intenta más tarde.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generarCarnet(ActionEvent e) {
        String nombreCompleto = nombreField.getText().trim();
        String initials = nombreCompleto.length() >= 2 ? nombreCompleto.substring(0, 2).toUpperCase() : "XX";
        int number = new Random().nextInt(9000) + 1000;
        String carnetGenerado = initials + number;
        carnetField.setText(carnetGenerado);
    }

    private void limpiarCampos() {
        originalCarnet = null;
        carnetField.setText("");
        nombreField.setText("");
        passwordField.setText("");
        rolComboBox.setSelectedIndex(0);
    }
}