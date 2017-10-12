package ar.com.crypticmind.dc.logging;

import ar.com.crypticmind.dc.Client;

public class Log4jLogger implements Logger {

    private final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Client.class);

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

}
