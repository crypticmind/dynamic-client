package ar.com.crypticmind.dc.clientlib.logging;

import ar.com.crypticmind.dc.clientlib.Client;

import java.util.logging.Level;

public class JavaLoggingLogger implements Logger {

    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Client.class.getName());

    @Override
    public void debug(String message) {
        logger.fine(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.log(java.util.logging.Level.SEVERE, message, t);
    }

    @Override
    public void warn(String message, Throwable t) {
        logger.log(Level.WARNING, message, t);
    }

}
