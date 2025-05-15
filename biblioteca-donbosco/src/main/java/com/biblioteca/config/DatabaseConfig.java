package com.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConfig {
    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                Properties props = new Properties();
                if (input == null) {
                    logger.error("No se encontró el archivo config.properties");
                    throw new RuntimeException("No se encontró el archivo config.properties");
                }
                props.load(input);

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                logger.info("Conexión exitosa a la base de datos.");
            } catch (Exception e) {
                logger.error("Error al conectar a la base de datos: {}", e.getMessage());
                throw new SQLException("No se pudo establecer conexión", e);
            }
        }
        return connection;
    }
}