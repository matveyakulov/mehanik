package ru.neirodev.mehanik.api.request;

import lombok.Data;

@Data
public class GetCarsRequest extends Request {

    private Long model;

    private Long make;

    private final String method = "getCars";

    private String group;

    private String key;

}
