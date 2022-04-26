package main.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TranslatorIntf extends Remote {
    /**
     * @param str - string to be translated
     * @param sourceLanguage - source language before translation
     * @param targetLanguage - target language after translation
     * @return answer or null if the dictionary doesn't contain this word
     * */
    String translate(String str, String sourceLanguage, String targetLanguage) throws RemoteException;
}
