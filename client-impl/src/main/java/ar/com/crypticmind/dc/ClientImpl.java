package ar.com.crypticmind.dc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ClientImpl implements Client {

    private URL endpoint;

    public ClientImpl(URL endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int sum(int a, int b) throws IOException {

        // Check server version, needs BuildInfo here (BuildInfo needs to go to a commons module)
        // URL versionUrl = new URL(endpoint.toString() + "/dynamic-client/version");

        URL sumUrl = new URL(endpoint.toString() + "/dynamic-client/service/sum/" + a + "/" + b);

        String result;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sumUrl.openStream()))) {
            result = reader.readLine();
            while (reader.read() != -1);
        }

        return Integer.parseInt(result);
    }

}
