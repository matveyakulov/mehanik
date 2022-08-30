package ru.neirodev.mehanik.api.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetModelsRequest extends Request {

    private Long make;
    private String group;
    private final String method = "getModels";
}
