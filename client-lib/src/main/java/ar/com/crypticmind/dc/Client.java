package ar.com.crypticmind.dc;

public interface Client {

    String version() throws Exception;

    int sum(int a, int b) throws Exception;

}
