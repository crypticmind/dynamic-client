package ar.com.crypticmind.dc.clientlib.logging;

public interface Logger {

    void debug(String message);

    void info(String message);

    void error(String message, Throwable t);

    void warn(String message, Throwable t);

}
