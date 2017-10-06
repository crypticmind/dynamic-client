package ar.com.crypticmind.dc;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

class Container implements AutoCloseable {

    private ClassLoader classLoader;
    private Client client;

    Container(Path jarFile, URL endpoint) throws Exception {
        classLoader = new URLClassLoader(new URL[] { jarFile.toUri().toURL() }, ClientProxy.class.getClassLoader());
        Class<?> impl = Class.forName("ar.com.crypticmind.dc.ClientImpl", true, classLoader);
        Constructor<?> ctor = impl.getDeclaredConstructor(URL.class);
        client = Client.class.cast(ctor.newInstance(endpoint));
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
