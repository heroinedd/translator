package main.client;

import main.server.TranslatorIntf;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private final static String HOST = "202.117.15.41";

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST);
            TranslatorIntf stub = (TranslatorIntf) registry.lookup("Translator");

            Scanner scanner = new Scanner(System.in);
            String in = "";
            while (true) {
                in = scanner.nextLine();
                if (in.equalsIgnoreCase("exit")) break;
                String[] parts = in.split(" ");
                System.out.println("Text: " + parts[0] + "\tSource Language: " + parts[1] + "\tTarget Language: " + parts[2]);
                String result = stub.translate(parts[0], parts[1], parts[2]);
                System.out.println("Result: " + result);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
