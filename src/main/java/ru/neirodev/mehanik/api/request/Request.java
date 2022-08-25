package ru.neirodev.mehanik.api.request;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;

import static ru.neirodev.mehanik.util.RestUtils.PARTSAPI_RU_API_PHP;

@Setter
public abstract class Request {

    @SneakyThrows
    public String getUri() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(PARTSAPI_RU_API_PHP);
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            uriComponentsBuilder.queryParam(field.getName(), field.get(this));
        }
        return uriComponentsBuilder.encode().toUriString();
    }
}
