package ru.neirodev.mehanik.api.service;

import ru.neirodev.mehanik.api.model.Make;
import ru.neirodev.mehanik.api.model.Model;

import java.util.List;

public interface ApiService {

    List<Make> getMakesRequest(String group);

    List<Model> getModelsRequest(Long make, String group);

    List<Model> getCarsRequest(Long make, Long model, String group);
}
