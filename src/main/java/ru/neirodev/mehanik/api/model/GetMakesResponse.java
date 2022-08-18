package ru.neirodev.mehanik.api.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetMakesResponse {

    private List<Make> makes;
}
