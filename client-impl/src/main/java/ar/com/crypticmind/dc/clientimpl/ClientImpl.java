package ar.com.crypticmind.dc.clientimpl;

import ar.com.crypticmind.dc.clientlib.Client;
import ar.com.crypticmind.dc.clientlib.HttpClient;
import ar.com.crypticmind.dc.clientlib.logging.Logger;

import java.io.IOException;
import java.net.URL;

import static ar.com.crypticmind.dc.clientlib.HttpClient.readString;

public class ClientImpl implements Client {

    private URL endpoint;
    private HttpClient httpClient;

    public ClientImpl(URL endpoint, Logger logger) {
        this.endpoint = endpoint;
        this.httpClient = new HttpClient(logger);
    }

    @Override
    public String version() throws Exception {
        return Version.VERSION;
    }

    @Override
    public int sum(int a, int b) throws IOException {
        return httpClient.doGET(
            new URL(endpoint.toString() + "/dynamic-client/service/sum/" + a + "/" + b),
            readString.andThen(Integer::parseInt)
        );
    }

}
