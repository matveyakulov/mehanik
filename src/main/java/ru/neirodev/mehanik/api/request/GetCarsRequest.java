package ru.neirodev.mehanik.api.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetCarsRequest extends Request {

    private Long make;
    private Long model;
    private String group;
    private final String method = "getCars";
}
