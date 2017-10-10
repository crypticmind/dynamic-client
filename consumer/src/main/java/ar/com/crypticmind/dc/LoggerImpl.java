package ar.com.crypticmind.dc;

public class LoggerImpl implements Logger {

    @Override
    public void debug(String message, Throwable t) {
        System.out.println("DEBUG: " + message + t);
        if (t != null)
            t.printStackTrace();
    }

    @Override
    public void info(String message, Throwable t) {
        System.out.println("INFO: " + message + t);
        if (t != null)
            t.printStackTrace();
    }

    @Override
    public void warn(String message, Throwable t) {
        System.out.println("WARN: " + message + t);
        if (t != null)
            t.printStackTrace();
    }

    @Override
    public void error(String message, Throwable t) {
        System.err.println("ERROR: " + message + t);
        if (t != null)
            t.printStackTrace();
    }

}
