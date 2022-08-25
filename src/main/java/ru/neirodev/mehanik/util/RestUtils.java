package ru.neirodev.mehanik.util;

import ru.neirodev.mehanik.api.model.CarPart;
import ru.neirodev.mehanik.api.model.CarPartFromJson;
import ru.neirodev.mehanik.api.model.Part;

import java.util.ArrayList;
import java.util.List;

public class RestUtils {

    public static final String PARTSAPI_RU_API_PHP = "https://partsapi.ru/api.php";

    public static String deleteAttributes(String body, String startWord, String endWord){
        StringBuilder builder = new StringBuilder(body);
        int indexStart = builder.indexOf(startWord);
        while (indexStart != -1){
            int indexEnd = builder.indexOf(endWord);
            builder.delete(indexStart, indexEnd + endWord.length());
            indexStart = builder.indexOf(startWord);
        }
        return builder.toString();
    }

    public static List<CarPart> getCarParts(List<CarPartFromJson> carPartFromJsons, String del){
        List<CarPart> carParts = new ArrayList<>();
        for (CarPartFromJson carPartFromJson : carPartFromJsons) {
            CarPart carPart = new CarPart();
            carPart.setName(carPartFromJson.getName());
            String[] parts = carPartFromJson.getParts().split(",");
            for (String part : parts) {
                int index = part.indexOf(del);
                Part partObj = new Part();
                partObj.setBrand(part.substring(0, index));
                partObj.setCode(part.substring(index + del.length()));
                carPart.getParts().add(partObj);
            }
            carParts.add(carPart);
        }
        return carParts;
    }
}
