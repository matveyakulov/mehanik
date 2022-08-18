package ru.neirodev.mehanik.api.service;

import ru.neirodev.mehanik.api.model.GetMakesResponse;

import java.util.List;
import java.util.Map;

public interface ApiService {

    GetMakesResponse sendGetMakesRequest(String group);
}
