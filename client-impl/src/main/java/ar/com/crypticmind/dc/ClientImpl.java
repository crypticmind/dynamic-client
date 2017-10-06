package ar.com.crypticmind.dc;

import java.io.IOException;
import java.net.URL;

import static ar.com.crypticmind.dc.HttpClient.doGET;
import static ar.com.crypticmind.dc.HttpClient.readString;

public class ClientImpl implements Client {

    private URL endpoint;

    ClientImpl(URL endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String version() throws Exception {
        return Version.VERSION;
    }

    @Override
    public int sum(int a, int b) throws IOException {
        return doGET(
            new URL(endpoint.toString() + "/dynamic-client/service/sum/" + a + "/" + b),
            readString.andThen(Integer::parseInt)
        );
    }

}
