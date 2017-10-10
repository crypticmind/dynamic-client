package ar.com.crypticmind.dc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Consumer {

    public static void main(String[] args) throws Exception {

        Logger logger = new Logger() {
            @Override
            public void debug(String message, Throwable t) {
                System.out.println("DEBUG: " + message + t);
                if (t != null)
                    t.printStackTrace();
            }

            @Override
            public void info(String message, Throwable t) {
                System.out.println("INFO: " + message + t);
                if (t != null)
                    t.printStackTrace();
            }

            @Override
            public void warn(String message, Throwable t) {
                System.out.println("WARN: " + message + t);
                if (t != null)
                    t.printStackTrace();
            }

            @Override
            public void error(String message, Throwable t) {
                System.err.println("ERROR: " + message + t);
                if (t != null)
                    t.printStackTrace();
            }
        };

        try (ClientProxy cp = new ClientProxy(new URL("http://localhost:8080"), logger)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Input a --> ");
            System.out.flush();
            int a = Integer.parseInt(reader.readLine());

            System.out.print("Input b --> ");
            System.out.flush();
            int b = Integer.parseInt(reader.readLine());

            Client client = cp.getClient().orElseThrow(() -> new Exception("No client available"));

            int sum = client.sum(a, b);
            System.out.println(String.valueOf(a) + " + " + String.valueOf(b) + " = " + String.valueOf(sum));

        }
    }
}
