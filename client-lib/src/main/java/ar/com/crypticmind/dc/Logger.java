package ar.com.crypticmind.dc;

public interface Logger {

    void debug(String message, Throwable t);

    void info(String message, Throwable t);

    void warn(String message, Throwable t);

    void error(String message, Throwable t);

}
