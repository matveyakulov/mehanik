package ru.neirodev.mehanik.api.request;

import lombok.Setter;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.Set;

import static ru.neirodev.mehanik.util.RestUtils.PARTSAPI_RU_API_PHP;

@Setter
public abstract class Request {

    private String key;

    @SneakyThrows
    public String getUri() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(PARTSAPI_RU_API_PHP);
        Set<Field> fields = ReflectionUtils.getAllFields(this.getClass(), (field) -> true);
        for (Field field : fields) {
            field.setAccessible(true);
            uriComponentsBuilder.queryParam(field.getName(), field.get(this));
        }
        return uriComponentsBuilder.encode().toUriString();
    }
}
