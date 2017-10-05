package ar.com.crypticmind.dc;

public interface Client {

    default String version() throws Exception {
        return "0";
    }

    default int sum(int a, int b) throws Exception {
        return 0;
    }

    static Client dummy() {
        return new Client() {};
    }
}
