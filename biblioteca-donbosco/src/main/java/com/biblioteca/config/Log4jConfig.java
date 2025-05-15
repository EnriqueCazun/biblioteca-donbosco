package com.biblioteca.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jConfig {
    private static final Logger logger = LogManager.getLogger(Log4jConfig.class);

    public static void init() {
        logger.info("Log4j 2 está configurado correctamente.");
    }
}