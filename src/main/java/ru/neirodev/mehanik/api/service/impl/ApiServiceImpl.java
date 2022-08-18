package ru.neirodev.mehanik.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neirodev.mehanik.api.model.GetMakesRequest;
import ru.neirodev.mehanik.api.model.GetMakesResponse;
import ru.neirodev.mehanik.api.service.ApiService;

import static ru.neirodev.mehanik.api.model.RestUtils.PARTSAPI_RU_API_PHP;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GetMakesResponse sendGetMakesRequest(String group) {
        GetMakesRequest request = new GetMakesRequest();
        request.setGroup(group);
        return restTemplate.getForObject(PARTSAPI_RU_API_PHP, GetMakesResponse.class, request.toMap());
    }
}
