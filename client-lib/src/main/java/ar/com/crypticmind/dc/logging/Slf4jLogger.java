package ar.com.crypticmind.dc.logging;

import ar.com.crypticmind.dc.Client;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Client.class);

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

    @Override
    public void warn(String message, Throwable t) {
        logger.warn(message, t);
    }

}
