package main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TranslatorImpl implements TranslatorIntf {
    private static final String GOOGLE_TRANSLATOR_URL = "https://script.google.com/macros/s/AKfycby13VqH0zKGZlGEGP_eeK3cUHZA4QoFiW-CFlzaXAn1RUV6P481jd-fmBZSZO6oS7s/exec";

    public TranslatorImpl() {}

    @Override
    public String translate(String str, String sourceLanguage, String targetLanguage) {
        StringBuilder response = new StringBuilder();
        try {
            String urlStr = GOOGLE_TRANSLATOR_URL +
                    "?q=" + URLEncoder.encode(str, "UTF-8") +
                    "&target=" + URLEncoder.encode(targetLanguage, "UTF-8") +
                    "&source=" + URLEncoder.encode(sourceLanguage, "UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Translate exception: " + e);
            e.printStackTrace();
        }
        return response.toString();
    }

    public static void main(String[] args) {
        try {
            TranslatorImpl translator = new TranslatorImpl();
            TranslatorIntf stub = (TranslatorIntf) UnicastRemoteObject.exportObject(translator, 2022);

            // Bind the remote object's stub in the registry
            LocateRegistry.createRegistry(1099);
            Naming.bind("Translator", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}
