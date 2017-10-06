package ar.com.crypticmind.dc;

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

class HttpClient {

    static <T> T doGET(URL url, Function<byte[], T> onSuccess) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (InputStream is = conn.getInputStream()) {
            byte[] buffer = read(is, conn.getContentLength());
            return onSuccess.apply(buffer);
        } catch (IOException e) {
            try (InputStream es = conn.getErrorStream()) {
                byte[] buffer = read(es, conn.getContentLength());
                int statusCode = conn.getResponseCode();
                throw new RuntimeException("GET " + url + " failed → HTTP " + statusCode + ": " + readString.apply(buffer));
            }
        }
    }

    static void download(URL url, Path target) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (InputStream is = conn.getInputStream()) {
            Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            try (InputStream es = conn.getErrorStream()) {
                byte[] buffer = read(es, conn.getContentLength());
                int statusCode = conn.getResponseCode();
                throw new RuntimeException("GET " + url + " failed → HTTP " + statusCode + ": " + readString.apply(buffer));
            }
        }
    }

    static final Consumer<byte[]> ignore = buffer -> {};

    static Function<byte[], String> readString = buffer -> {
        try {
            return new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not read UTF-8 text response", e);
        }
    };

    private static byte[] read(InputStream is, int length) throws IOException {
        byte[] buffer = new byte[length];
        int pos = 0;
        int b;
        while ((b = is.read()) > 0)
            buffer[pos++] = (byte) b;
        return buffer;
    }
}
