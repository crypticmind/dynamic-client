package ar.com.crypticmind.dc.logging;

import ar.com.crypticmind.dc.Client;

public class CommonsLoggingLogger implements Logger {

    private final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(Client.class.getPackage().getName());

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

}
