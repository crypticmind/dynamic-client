package ar.com.crypticmind.dc;

public class ClientProxy {

    private static Client INSTANCE = null;

    public static Client getClient() {
        if (INSTANCE == null) {
            synchronized (Client.class) {
                INSTANCE = (a, b) -> a + b;
            }
        }
        return INSTANCE;
    }

}
