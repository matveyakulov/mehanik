package ru.neirodev.mehanik.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neirodev.mehanik.api.model.*;
import ru.neirodev.mehanik.service.ApiService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static ru.neirodev.mehanik.util.RestUtils.deleteAttributes;

@Service
public class ApiServiceImpl implements ApiService {

    private RestTemplate restTemplate;

    @Value("${partsapi.getMakes.key}")
    private String getMakesKey;

    @Value("${partsapi.getModels.key}")
    private String getModelsKey;

    @Value("${partsapi.getCars.key}")
    private String getCarsKey;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }

    @SneakyThrows
    @Override
    public List<Make> getMakesRequest(String group) {
        GetMakesRequest request = new GetMakesRequest();
        request.setGroup(group);
        request.setKey(getMakesKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(request.getUri(), GET, entity, String.class, new HashMap<>()).getBody();
        body = body != null ? body.replace('\'', '\"') : null;
        if (body != null) {
            return new ObjectMapper().readValue(body, new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "models", key = "#make")
    @SneakyThrows
    @Override
    public List<Model> getModelsRequest(Long make, String group) {
        GetModelsRequest request = new GetModelsRequest();
        request.setMake(make);
        request.setGroup(group);
        request.setKey(getModelsKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(request.getUri(), GET, entity, String.class, new HashMap<>()).getBody();
        body = body != null ? body.replace('\'', '\"') : null;
        if (body != null) {
            return new ObjectMapper().readValue(body, new TypeReference<>() {
            });
        }
        return Collections.emptyList();

    }

    @Cacheable(value = "cars", key = "#model")
    @SneakyThrows
    @Override
    public List<Car> getCarsRequest(Long make, Long model, String group) {
        GetCarsRequest request = new GetCarsRequest();
        request.setMake(make);
        request.setModel(model);
        request.setGroup(group);
        request.setKey(getCarsKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(request.getUri(), GET, entity, String.class, new HashMap<>()).getBody();
        body = body != null ? StringUtils.replace(body, "'", "\"") : null;
        if (body != null) {
            body = deleteAttributes(body);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(body, new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }

}
