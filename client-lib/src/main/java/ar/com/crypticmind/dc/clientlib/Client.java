package ar.com.crypticmind.dc.clientlib;

public interface Client {

    String version() throws Exception;

    int sum(int a, int b) throws Exception;

}
