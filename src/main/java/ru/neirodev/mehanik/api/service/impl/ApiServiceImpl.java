package ru.neirodev.mehanik.api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neirodev.mehanik.api.model.GetMakesRequest;
import ru.neirodev.mehanik.api.model.GetModelsRequest;
import ru.neirodev.mehanik.api.model.Make;
import ru.neirodev.mehanik.api.model.Model;
import ru.neirodev.mehanik.api.service.ApiService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static ru.neirodev.mehanik.util.RestUtils.parseJson;

@Service
public class ApiServiceImpl implements ApiService {

    private RestTemplate restTemplate;

    @Value("${partsapi.getMakes.key}")
    private String getMakesKey;

    @Value("${partsapi.getModels.key}")
    private String getModelsKey;

    @PostConstruct
    public void init(){
        restTemplate = new RestTemplate();
    }

    @Override
    public List<Make> getMakesRequest(String group) {
        GetMakesRequest request = new GetMakesRequest();
        request.setGroup(group);
        request.setKey(getMakesKey);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(request.getUri(), GET, entity, String.class, new HashMap<>()).getBody();
        return parseJson(body, Make.class);
    }

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
        return parseJson(body, Model.class);
    }

    @Override
    public List<Model> getCarsRequest(Long make, Long model, String group) {
        return null;
    }

}
