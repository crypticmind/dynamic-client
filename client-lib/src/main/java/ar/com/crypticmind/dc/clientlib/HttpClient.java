package ar.com.crypticmind.dc.clientlib;

import ar.com.crypticmind.dc.clientlib.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.function.Function;

public class HttpClient {

    public HttpClient(Logger logger) {
        this.logger = logger;
    }

    public <T> T doGET(URL url, Function<byte[], T> onSuccess) throws IOException {
        logger.debug("GET " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (InputStream is = conn.getInputStream()) {
            byte[] buffer = read(is, conn.getContentLength());
            return onSuccess.apply(buffer);
        } catch (IOException e) {
            throw httpError(url, conn, e);
        }
    }

    public void download(URL url, Path target) throws IOException {
        logger.debug("GET " + url + " to " + target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (InputStream is = conn.getInputStream()) {
            Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw httpError(url, conn, e);
        }
    }

    public static final Consumer<byte[]> ignore = buffer -> {};

    public static Function<byte[], String> readString = buffer -> {
        try {
            return new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not read UTF-8 text response", e);
        }
    };

    private Logger logger;

    private RuntimeException httpError(URL url, HttpURLConnection conn, IOException e) throws IOException {
        int statusCode = conn.getResponseCode();
        if (statusCode != -1) {
            String response;
            try (InputStream es = conn.getErrorStream()) {
                byte[] buffer = read(es, conn.getContentLength());
                response = readString.apply(buffer);
            }
            return new RuntimeException("GET " + url + " failed → HTTP " + statusCode + ": " + response);
        } else
            return new RuntimeException("GET " + url + " failed → " + e.getMessage());
    }

    private byte[] read(InputStream is, int length) throws IOException {
        byte[] buffer = new byte[length];
        int pos = 0;
        int b;
        while ((b = is.read()) > 0)
            buffer[pos++] = (byte) b;
        return buffer;
    }
}
