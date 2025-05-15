package com.biblioteca.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.biblioteca.config.Log4jConfig;
import com.biblioteca.config.DatabaseConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUserWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(RegisterUserWindow.class);

    private JComboBox<String> rolComboBox;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField carnetField;
    private JPasswordField passwordField;
    private JPasswordField repetirPasswordField;
    private JButton generarCarnetButton;
    private JButton registrarButton;
    private JButton cancelarButton;

    public RegisterUserWindow() {
        Log4jConfig.init();

        setTitle("Registro de Usuario");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel rolLabel = new JLabel("Selecciona tu rol:");
        rolComboBox = new JComboBox<>(new String[]{"Alumno", "Profesor", "Administrador"});

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(15);

        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoField = new JTextField(15);

        JLabel carnetLabel = new JLabel("Carnet:");
        carnetField = new JTextField(15);
        carnetField.setEditable(false);

        generarCarnetButton = new JButton("Generar Carnet");

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);

        JLabel repetirPasswordLabel = new JLabel("Repetir Contraseña:");
        repetirPasswordField = new JPasswordField(15);

        registrarButton = new JButton("Registrar");
        cancelarButton = new JButton("Cancelar");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(rolLabel, gbc);
        gbc.gridx = 1;
        panel.add(rolComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(nombreLabel, gbc);
        gbc.gridx = 1;
        panel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(apellidoLabel, gbc);
        gbc.gridx = 1;
        panel.add(apellidoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(carnetLabel, gbc);
        gbc.gridx = 1;
        panel.add(carnetField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(generarCarnetButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(repetirPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(repetirPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(registrarButton, gbc);
        gbc.gridx = 1;
        panel.add(cancelarButton, gbc);

        add(panel);

        generarCarnetButton.addActionListener(e -> generarCarnet());

        cancelarButton.addActionListener(e -> dispose());

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                String apellido = apellidoField.getText().trim();
                String carnet = carnetField.getText().trim();
                String pass = new String(passwordField.getPassword());
                String repetirPass = new String(repetirPasswordField.getPassword());

                if (nombre.isEmpty() || apellido.isEmpty() || carnet.isEmpty() || pass.isEmpty() || repetirPass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor llena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                    logger.warn("Campos incompletos al intentar registrar un usuario.");
                    return;
                }

                if (!pass.equals(repetirPass)) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.error("Las contraseñas no coinciden al intentar registrar un usuario.");
                    return;
                }

                if (registrarUsuario(nombre, apellido, carnet, pass)) {
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Usuario registrado exitosamente: " + carnet);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar usuario. Intenta nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.error("Error al registrar el usuario: " + carnet);
                }
            }
        });
    }

    private void generarCarnet() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa nombre y apellido antes de generar el carnet.", "Información faltante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        char inicialNombre = Character.toUpperCase(nombre.charAt(0));
        char inicialApellido = Character.toUpperCase(apellido.charAt(0));
        int numeroAleatorio = new Random().nextInt(9000) + 1000;

        String carnetGenerado = "" + inicialNombre + inicialApellido + numeroAleatorio;
        carnetField.setText(carnetGenerado);
    }

    private boolean registrarUsuario(String nombre, String apellido, String carnet, String password) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "INSERT INTO usuarios (nombre, apellido, carnet, password, rol) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, carnet);
            stmt.setString(4, password);
            stmt.setString(5, rolComboBox.getSelectedItem().toString());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            logger.error("Error al registrar el usuario en la base de datos: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterUserWindow().setVisible(true));
    }
}
