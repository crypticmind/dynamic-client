package ar.com.crypticmind.dc.clientlib;

import ar.com.crypticmind.dc.clientlib.logging.Logger;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

class Container implements AutoCloseable {

    private ClassLoader classLoader;
    private Client client;

    Container(Path jarFile, URL endpoint, Logger logger) throws Exception {
        classLoader = new URLClassLoader(new URL[] { jarFile.toUri().toURL() }, ClientProxy.class.getClassLoader());
        Class<?> impl = Class.forName("ar.com.crypticmind.dc.clientimpl.ClientImpl", true, classLoader);
        Constructor<?> ctor = impl.getDeclaredConstructor(URL.class, Logger.class);
        client = Client.class.cast(ctor.newInstance(endpoint, logger));
    }

    Client getClient() {
        if (client == null)
            throw new IllegalStateException("Container has been closed");
        return client;
    }

    @Override
    public void close() throws Exception {
        if (classLoader instanceof AutoCloseable)
            ((AutoCloseable)classLoader).close();
        client = null;
        classLoader = null;
    }
}
