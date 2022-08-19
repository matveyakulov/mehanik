package ru.neirodev.mehanik.util;

public class RestUtils {

    public static final String PARTSAPI_RU_API_PHP = "https://partsapi.ru/api.php";

    public static String deleteAttributes(String body){
        StringBuilder builder = new StringBuilder(body);
        String startWord = ", \"attributes\"";
        String endWord = "\"passenger\"";
        int indexStart = builder.indexOf(startWord);
        while (indexStart != -1){
            int indexEnd = builder.indexOf(endWord);
            builder.delete(indexStart, indexEnd + endWord.length());
            indexStart = builder.indexOf(startWord);
        }
        return builder.toString();
    }
}
