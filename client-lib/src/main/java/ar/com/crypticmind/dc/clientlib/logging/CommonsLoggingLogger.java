package ar.com.crypticmind.dc.clientlib.logging;

import ar.com.crypticmind.dc.clientlib.Client;

public class CommonsLoggingLogger implements Logger {

    private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(Client.class);

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
