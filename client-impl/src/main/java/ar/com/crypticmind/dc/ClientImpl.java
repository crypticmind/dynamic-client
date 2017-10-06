package ar.com.crypticmind.dc;

import java.io.IOException;
import java.net.URL;

import static ar.com.crypticmind.dc.HttpClient.doGET;
import static ar.com.crypticmind.dc.HttpClient.readString;

public class ClientImpl implements Client {

    private URL endpoint;

    public ClientImpl(URL endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int sum(int a, int b) throws IOException {
        return doGET(
            new URL(endpoint.toString() + "/dynamic-client/service/sum/" + a + "/" + b),
            readString.andThen(Integer::parseInt)
        );
    }

}
