package ar.com.crypticmind.dc;

import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpRequest;
import org.mockserver.socket.PortFactory;

public class ClientProxyTest {

    private int port = PortFactory.findFreePort();
    private ClientAndServer server = ClientAndServer.startClientAndServer(port);

    @Test
    public void test() {

        server.when(HttpRequest.request().withPath("/dynamic-client/version"))
                .respond(HttpResponse.response());

    }
}
