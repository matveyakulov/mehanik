package ru.neirodev.mehanik.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class RestUtils {

    public static final String PARTSAPI_RU_API_PHP = "https://partsapi.ru/api.php";

    @SneakyThrows
    public static <T> List<T> parseJson(String body, Class<T> aClass){
        body = body.substring(1, body.length() - 1);
        body = body.replace('\'', '\"');
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> makes = new ArrayList<>();
        int indexStart = 0;
        int indexEnd = body.indexOf("},");
        while (indexEnd != -1) {
            String substring;
            if(indexStart != 0) {
                substring = body.substring(indexStart + 3, indexEnd + 1);
            } else {
                substring = body.substring(indexStart, indexEnd + 1);
            }
            T element = objectMapper.readValue(substring, aClass);
            makes.add(element);
            indexStart = indexEnd;
            indexEnd = body.indexOf("},", indexStart + 2);
        }
        return makes;
    }
}
