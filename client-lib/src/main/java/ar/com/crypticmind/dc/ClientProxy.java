package ar.com.crypticmind.dc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static ar.com.crypticmind.dc.HttpClient.*;

public class ClientProxy implements AutoCloseable {

    public ClientProxy(URL endpoint, Logger logger) {
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
                Container nc = new Container(localLib, endpoint);
                logger.info("Successfully initialized client version " + version, null);
                Container pc = container.getAndSet(nc);
                if (pc == null)
                    executor.scheduleWithFixedDelay(checkServerVersion, checkServerVersionEvery.toMillis(), checkServerVersionEvery.toMillis(), TimeUnit.MILLISECONDS);
            } catch (Exception ex) {
                logger.error("Could not pull client jar from server. Retrying in 5 seconds. Exception: ", ex);
                executor.schedule(pullServerClient, retryInitializationEvery.toMillis(), TimeUnit.MILLISECONDS);
            }
        };
        checkServerVersion = () -> Optional
                .ofNullable(container.get())
                .map(Container::getClient)
                .flatMap(client -> {
                    try {
                        return Optional.ofNullable(client.version());
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                })
                .ifPresent(localVersion -> {
                    try {
                        String serverVersion = doGET(new URL(endpoint.toString() + "/dynamic-client/version"), readString);
                        if (!localVersion.equals(serverVersion)) {
                            System.out.println("Local version " + localVersion + ", server version " + serverVersion + ". Updating local client...");
                            executor.submit(pullServerClient);
                        }
                    } catch (Exception ex) {
                        System.err.println("Could not check server version. Exception: " + ex);
                    }
                });
        executor.submit(pullServerClient);
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

    Duration retryInitializationEvery = Duration.ofSeconds(5);
    Duration checkServerVersionEvery = Duration.ofMinutes(15);

    private AtomicReference<Container> container;
    private ScheduledExecutorService executor;
    private Runnable pullServerClient;
    private Runnable checkServerVersion;

}
