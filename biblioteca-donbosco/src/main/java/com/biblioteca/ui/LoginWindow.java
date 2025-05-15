package com.biblioteca.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.biblioteca.services.*;
import com.biblioteca.config.Log4jConfig;
import com.biblioteca.config.DatabaseConfig;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(LoginWindow.class);

    private JComboBox<String> rolComboBox;
    private JTextField carnetField;
    private JPasswordField passwordField;
    private JButton ingresarButton;
    private JButton registrarButton;
    private JButton restablecerContrasenaButton;
    private JButton probarConexionButton;

    public LoginWindow() {
        Log4jConfig.init();

        setTitle("Inicio de Sesión - Biblioteca Don Bosco");
        setSize(1720, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel rolLabel = new JLabel("Selecciona tu rol:");
        rolComboBox = new JComboBox<>(new String[]{"Alumno", "Profesor", "Administrador"});

        JLabel carnetLabel = new JLabel("Carnet:");
        carnetField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);

        ingresarButton = new JButton("Ingresar");
        registrarButton = new JButton("Registrarte");
        restablecerContrasenaButton = new JButton("Restablecer contraseña");
        probarConexionButton = new JButton("Probar conexión");

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(rolLabel, gbc);
        gbc.gridx = 1;
        panel.add(rolComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(carnetLabel, gbc);
        gbc.gridx = 1;
        panel.add(carnetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(ingresarButton, gbc);
        gbc.gridx = 1;
        panel.add(registrarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(restablecerContrasenaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(probarConexionButton, gbc);

        add(panel);

        rolComboBox.addActionListener(e -> {
            String rol = rolComboBox.getSelectedItem().toString();
            restablecerContrasenaButton.setVisible(!rol.equals("Administrador"));
        });

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rolSeleccionado = rolComboBox.getSelectedItem().toString();
                String carnet = carnetField.getText();
                String password = new String(passwordField.getPassword());

                if (carnet.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.error("Intento de inicio de sesión con campos vacíos.");
                    return;
                }

                if (validarCredenciales(carnet, password, rolSeleccionado)) {
                    logger.info("Inicio de sesión exitoso para el rol: " + rolSeleccionado + " y carnet: " + carnet);
                    switch (rolSeleccionado) {
                        case "Alumno":
                            new AlumnoService(carnet).setVisible(true);
                            dispose();
                            break;
                        case "Profesor":
                            new ProfesorService(carnet).setVisible(true);
                            dispose();
                            break;
                        case "Administrador":
                            new AdminService(carnet).setVisible(true);
                            dispose();
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas o rol no coincide con el registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    logger.error("Intento de inicio de sesión fallido para el carnet: " + carnet);
                }
            }
        });

        restablecerContrasenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rolSeleccionado = rolComboBox.getSelectedItem().toString();
                new ResetPasswordWindow(rolSeleccionado).setVisible(true);
            }
        });

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterUserWindow().setVisible(true);
            }
        });

        probarConexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (testDatabaseConnection()) {
                    JOptionPane.showMessageDialog(null, "Conexión exitosa a la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean testDatabaseConnection() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al intentar conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    private boolean validarCredenciales(String carnet, String password, String rolSeleccionado) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String query = "SELECT * FROM usuarios WHERE carnet = ? AND password = ? AND rol = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, carnet);
            stmt.setString(2, password);
            stmt.setString(3, rolSeleccionado);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }
}