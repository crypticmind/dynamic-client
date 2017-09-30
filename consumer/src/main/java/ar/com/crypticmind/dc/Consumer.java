package ar.com.crypticmind.dc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Consumer {

    public static void main(String[] args) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        Client client = ClientFactory.getClient(new URL("http://localhost:8080"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Input a --> ");
        System.out.flush();
        int a = Integer.parseInt(reader.readLine());

        System.out.print("Input b --> ");
        System.out.flush();
        int b = Integer.parseInt(reader.readLine());

        int sum = client.sum(a, b);
        System.out.println(String.valueOf(a) + " + " + String.valueOf(b) + " = " + String.valueOf(sum));
    }
}
