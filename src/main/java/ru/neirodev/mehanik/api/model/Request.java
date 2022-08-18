package ru.neirodev.mehanik.api.model;

import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class Request {

    @SneakyThrows
    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            params.put(field.getName(), (String) field.get(this));
        }
        return params;
    }
}
