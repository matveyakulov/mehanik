package ru.neirodev.mehanik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neirodev.mehanik.api.model.GetMakesResponse;
import ru.neirodev.mehanik.api.service.ApiService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/makes")
    public GetMakesResponse getMakes(@RequestParam String group){
        return apiService.sendGetMakesRequest(group);
    }
}
