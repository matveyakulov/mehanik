package ru.neirodev.mehanik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neirodev.mehanik.api.model.Car;
import ru.neirodev.mehanik.api.model.Make;
import ru.neirodev.mehanik.api.model.Model;
import ru.neirodev.mehanik.service.ApiService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/makes")
    public List<Make> getMakes(@RequestParam final String group){
        return apiService.getMakesRequest(group);
    }

    @GetMapping("/models")
    public List<Model> getModels(@RequestParam final Long make, @RequestParam final String group){
        return apiService.getModelsRequest(make, group);
    }

    @GetMapping("/cars")
    public List<Car> getCars(@RequestParam final Long make, @RequestParam final Long model, @RequestParam final String group){
        return apiService.getCarsRequest(make, model, group);
    }
}
