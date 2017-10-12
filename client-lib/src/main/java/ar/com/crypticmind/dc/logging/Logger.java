package ar.com.crypticmind.dc.logging;

public interface Logger {

    void info(String message);

    void error(String message, Throwable t);

}
