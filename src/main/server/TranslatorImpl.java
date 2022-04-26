package main.server;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TranslatorImpl implements TranslatorIntf {
    private static final String BAIDU_TRANSLATOR_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    private static final String BAIDU_TRANSLATOR_APPID = "20220426001191873";
    private static final String KEY = "AAASbsBCdjjgpKoo09AA";

    public TranslatorImpl() {}

    private String getQuery(String str, String srcLang, String tarLang) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        long salt = new Random().nextLong();

        String sign = BAIDU_TRANSLATOR_APPID + str + salt + KEY;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(sign.getBytes(StandardCharsets.UTF_8));
        sign = new BigInteger(1, md5.digest()).toString(16);

        return BAIDU_TRANSLATOR_URL +
                "?q=" + URLEncoder.encode(str, "UTF-8") +
                "&from=" + srcLang +
                "&to=" + tarLang +
                "&appid=" + BAIDU_TRANSLATOR_APPID +
                "&salt=" + salt +
                "&sign=" + sign;
    }

    @Override
    public String translate(String str, String sourceLanguage, String targetLanguage) {
        System.out.println("Text: " + str + "\tSource Language: " + sourceLanguage + "\tTarget Language: " + targetLanguage);
        StringBuilder response = new StringBuilder();
        try {
            String urlStr = getQuery(str, sourceLanguage, targetLanguage);
            URL url = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException | NoSuchAlgorithmException e) {
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
