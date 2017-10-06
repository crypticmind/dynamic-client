package ar.com.crypticmind.dc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static ar.com.crypticmind.dc.HttpClient.doGET;
import static ar.com.crypticmind.dc.HttpClient.download;
import static ar.com.crypticmind.dc.HttpClient.readString;

public class ClientProxy implements AutoCloseable {

    public ClientProxy(URL endpoint) {
        container = new AtomicReference<>();
        executor = Executors.newSingleThreadScheduledExecutor();
        pullServerClient = () -> {
            try {
                Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
                String version = doGET(new URL(endpoint.toString() + "/dynamic-client/version"), readString);
                if (version == null || version.trim().isEmpty())
                    throw new Exception("Got null or empty version from server");
                Path localLib = tmpDir.resolve("dc-client-impl-" + version + ".jar");
                Path tmpLib = Files.createTempFile("dc-client-impl-" + version + ".jar.download-", "");
                if (!Files.exists(localLib) || version.endsWith("SNAPSHOT")) {
                    download(new URL(endpoint.toString() + "/dynamic-client/library"), tmpLib);
                    Files.copy(tmpLib, localLib, StandardCopyOption.REPLACE_EXISTING);
                }
                Container c = new Container(localLib, endpoint);
                System.out.println("Successfully initialized client version " + version);
                container.set(c);
            } catch (Exception ex) {
                System.err.println("Could not pull client jar from server. Retrying in 5 seconds. Exception: " + ex);
                executor.schedule(pullServerClient, 5, TimeUnit.SECONDS);
            }
        };
        executor.schedule(pullServerClient, 0, TimeUnit.SECONDS);
    }

    public Optional<Client> getClient() {
        return Optional.ofNullable(container.get()).map(Container::getClient);
    }

    @Override
    public void close() throws Exception {
        executor.shutdownNow();
        Container c = container.getAndSet(null);
        if (c != null)
            c.close();
    }

    private AtomicReference<Container> container;
    private ScheduledExecutorService executor;
    private Runnable pullServerClient;


}
