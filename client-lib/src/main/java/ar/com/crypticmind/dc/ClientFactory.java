package ar.com.crypticmind.dc;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static ar.com.crypticmind.dc.HttpClient.*;

public class ClientFactory {

    public static Client getClient(URL endpoint) throws Exception {


        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));

        String version = doGET(new URL(endpoint.toString() + "/dynamic-client/version"), readString);

        if (version == null || version.trim().isEmpty())
            return Client.dummy();

        Path localLib = tmpDir.resolve("dc-client-impl-" + version + ".jar");
        Path tmpLib = Files.createTempFile("dc-client-impl-" + version + ".jar.download-", "");

        if (!Files.exists(localLib) || version.endsWith("SNAPSHOT")) {
            download(new URL(endpoint.toString() + "/dynamic-client/library"), tmpLib);
            Files.copy(tmpLib, localLib, StandardCopyOption.REPLACE_EXISTING);
        }

        return loadLib(endpoint, localLib);
    }

    private static Client loadLib(URL endpoint, Path localLib) throws Exception {
        URLClassLoader ucl = new URLClassLoader(new URL[] { localLib.toUri().toURL() }, ClientFactory.class.getClassLoader());
        Class<?> impl = Class.forName("ar.com.crypticmind.dc.ClientImpl", true, ucl);
        Constructor<?> ctor = impl.getDeclaredConstructor(URL.class);
        return Client.class.cast(ctor.newInstance(endpoint));
    }

}
