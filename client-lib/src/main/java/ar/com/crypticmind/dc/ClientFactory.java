package ar.com.crypticmind.dc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ClientFactory {

    public static Client getClient(URL endpoint) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        URL versionUrl = new URL(endpoint.toString() + "/dynamic-client/version");
        URL libUrl = new URL(endpoint.toString() + "/dynamic-client/library");
        Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));

        String version;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(versionUrl.openStream()))) {
            version = reader.readLine();
            System.out.println("Got version " + version + " from server");
            while (reader.read() != -1);
        }

        if (version == null) {
            System.out.println("Got NO version from server. Defaulting to dummy implementation.");
            return (a, b) -> 0;
        }

        Path localLib = tmpDir.resolve("dc-client-impl-" + version + ".jar");
        Path tmpLib = Files.createTempFile("dc-client-impl-" + version + ".jar.download-", "");

        if (!Files.exists(localLib) || version.endsWith("SNAPSHOT")) {
            System.out.println(localLib.toString() + " does not exist or is a snapshot. Downloading ...");
            try (InputStream is = libUrl.openStream()) {
                Files.copy(is, tmpLib, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded to temporary location " + tmpLib.toString());
            }
            Files.copy(tmpLib, localLib, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied temporary copy to " + localLib.toString());
        }

        return loadLib(localLib);
    }

    private static Client loadLib(Path localLib) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URLClassLoader ucl = new URLClassLoader(new URL[] { localLib.toUri().toURL() }, ClientFactory.class.getClassLoader());
        Class impl = Class.forName("ar.com.crypticmind.dc.ClientImpl", true, ucl);
        return (Client) impl.newInstance();
    }

}
