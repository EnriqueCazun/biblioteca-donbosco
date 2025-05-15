package com.biblioteca.ui;

import com.biblioteca.dao.ResetPasswordDAO;
import javax.swing.*;
import java.awt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetPasswordWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(ResetPasswordWindow.class);

    private JTextField carnetField;
    private JPasswordField nuevaPasswordField;
    private JPasswordField repetirPasswordField;
    private JButton enviarSolicitudButton;
    private String rol;

    public ResetPasswordWindow(String rol) {
        this.rol = rol;

        setTitle("Solicitud de Restablecimiento - " + rol);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel infoLabel = new JLabel("Solicitud de cambio de contraseña");
        JLabel carnetLabel = new JLabel("Carnet:");
        JLabel nuevaPasswordLabel = new JLabel("Nueva contraseña:");
        JLabel repetirPasswordLabel = new JLabel("Repetir nueva contraseña:");

        carnetField = new JTextField(15);
        nuevaPasswordField = new JPasswordField(15);
        repetirPasswordField = new JPasswordField(15);
        enviarSolicitudButton = new JButton("Enviar solicitud");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(infoLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(carnetLabel, gbc);
        gbc.gridx = 1;
        panel.add(carnetField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(nuevaPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(nuevaPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(repetirPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(repetirPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(enviarSolicitudButton, gbc);

        add(panel);

        enviarSolicitudButton.addActionListener(e -> {
            String carnet = carnetField.getText().trim();
            String nuevaPass = new String(nuevaPasswordField.getPassword());
            String repetirPass = new String(repetirPasswordField.getPassword());

            if (carnet.isEmpty() || nuevaPass.isEmpty() || repetirPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                logger.warn("Solicitud incompleta de restablecimiento de contraseña.");
                return;
            }

            if (!nuevaPass.equals(repetirPass)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                logger.warn("Contraseñas no coinciden para carnet: " + carnet);
                return;
            }

            ResetPasswordDAO resetPasswordDAO = new ResetPasswordDAO();
            boolean exito = resetPasswordDAO.insertarSolicitud(rol, carnet, nuevaPass);

            if (exito) {
                logger.info("Solicitud de restablecimiento registrada para " + rol + " con carnet: " + carnet);
                JOptionPane.showMessageDialog(this,
                        "Solicitud enviada. Administración revisará tu solicitud.",
                        "Solicitud enviada",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                logger.error("Error al registrar solicitud de restablecimiento para " + carnet);
                JOptionPane.showMessageDialog(this,
                        "Hubo un error al enviar la solicitud. Intenta nuevamente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResetPasswordWindow("Alumno").setVisible(true));
    }
}