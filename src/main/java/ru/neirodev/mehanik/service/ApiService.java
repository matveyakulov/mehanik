package ru.neirodev.mehanik.service;

import ru.neirodev.mehanik.api.model.*;

import java.util.List;

public interface ApiService {

    List<Make> getMakesRequest(String group);

    List<Model> getModelsRequest(Long make, String group);

    List<Car> getCarsRequest(Long make, Long model, String group);

    List<CarPart> carPartsList(String typeid, String kid);
}
