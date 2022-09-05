package ru.neirodev.mehanik.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neirodev.mehanik.api.model.*;
import ru.neirodev.mehanik.api.request.*;
import ru.neirodev.mehanik.service.ApiService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static ru.neirodev.mehanik.util.RestUtils.deleteAttributes;
import static ru.neirodev.mehanik.util.RestUtils.getCarParts;

@Service
public class ApiServiceImpl implements ApiService {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @Value("${partsapi.getMakes.key}")
    private String getMakesKey;

    @Value("${partsapi.getModels.key}")
    private String getModelsKey;

    @Value("${partsapi.getCars.key}")
    private String getCarsKey;

    @Value("${partsapi.carPartsList.key}")
    private String carPartsListKey;

    @Value("${partsapi.vinDecodeShort.key}")
    private String vinDecodeKey;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    @Cacheable(value = "makes", key = "#group")
    @Override
    public List<Make> getMakesRequest(String group) {
        GetMakesRequest request = new GetMakesRequest(group);
        request.setKey(getMakesKey);
        String body = getBodyFromRequest(request);
        if (body != null) {
            try {
                return new ObjectMapper().readValue(body, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "models", key = "#make")
    @Override
    public List<Model> getModelsRequest(Long make, String group) {
        GetModelsRequest request = new GetModelsRequest(make, group);
        request.setKey(getModelsKey);
        String body = getBodyFromRequest(request);
        if (body != null) {
            try {
                return new ObjectMapper().readValue(body, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "cars", key = "#model")
    @Override
    public List<Car> getCarsRequest(Long make, Long model, String group) {
        GetCarsRequest request = new GetCarsRequest(make, model, group);
        request.setKey(getCarsKey);
        String body = getBodyFromRequest(request);
        if (body != null) {
            String startWord = ", \"attributes\"";
            String endWord = "\"passenger\"";
            body = deleteAttributes(body, startWord, endWord);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(body, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "carParts", key = "#kid")
    @Override
    public List<CarPart> carPartsList(String typeid, String kid) {
        CarPartsListRequest request = new CarPartsListRequest(typeid, kid);
        request.setKey(carPartsListKey);
        String body = getBodyFromRequest(request);
        List<CarPartFromJson> carPartFromJsons;
        try {
            carPartFromJsons = objectMapper.readValue(body, new TypeReference<>() {
            });
            return getCarParts(carPartFromJsons, request.getDel());
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "vins", key = "#vin")
    @Override
    public List<VinDecode> vinDecodeShort(String vin) {
        VinDecodeShortRequest request = new VinDecodeShortRequest(vin);
        request.setKey(vinDecodeKey);
        String body = getBodyFromRequest(request);
        if (body != null) {
            try {
                return objectMapper.readValue(body, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    private String getBodyFromRequest(Request request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String body = restTemplate.exchange(request.getUri(), GET, entity, String.class, new HashMap<>()).getBody();
        return body != null ? StringUtils.replace(body, "'", "\"") : null;
    }
}
