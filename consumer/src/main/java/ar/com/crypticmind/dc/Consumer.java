package ar.com.crypticmind.dc;

import ar.com.crypticmind.dc.logging.Slf4jLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
//import java.util.Random;

public class Consumer {

    public static void main(String[] args) throws Exception {

        try (ClientProxy cp = new ClientProxy(new URL("http://localhost:8080"), new Slf4jLogger())) {

//            while (cp.getClient() == null)
//                Thread.sleep(200);
//
//            Random r = new Random();
//
//            while (true) {
//                int a = r.nextInt(1000000);
//                int b = r.nextInt(1000000);
//                try {
//                    cp.getClient().sum(a, b);
//                } catch (Exception e) {
//                }
//            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Input a --> ");
            System.out.flush();
            int a = Integer.parseInt(reader.readLine());

            System.out.print("Input b --> ");
            System.out.flush();
            int b = Integer.parseInt(reader.readLine());

            Client client = Optional.ofNullable(cp.getClient()).orElseThrow(() -> new Exception("No client available"));

            int sum = client.sum(a, b);
            System.out.println(String.valueOf(a) + " + " + String.valueOf(b) + " = " + String.valueOf(sum));

        }
    }
}
