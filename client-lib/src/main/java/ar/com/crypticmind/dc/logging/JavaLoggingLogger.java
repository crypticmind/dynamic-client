package ar.com.crypticmind.dc.logging;

import ar.com.crypticmind.dc.Client;

public class JavaLoggingLogger implements Logger {

    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Client.class.getPackage().getName());

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.log(java.util.logging.Level.SEVERE, message, t);
    }

}
