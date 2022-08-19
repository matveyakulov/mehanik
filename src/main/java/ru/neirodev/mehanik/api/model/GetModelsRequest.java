package ru.neirodev.mehanik.api.model;

import lombok.Data;

@Data
public class GetModelsRequest extends Request {

    private Long make;

    private final String method = "getModels";

    private String group;

    private String key;

}
