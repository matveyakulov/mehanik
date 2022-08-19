package ru.neirodev.mehanik.api.model;

import lombok.Data;

@Data
public class GetCarsRequest extends Request {

    private Long model;

    private Long make;

    private final String method = "getCars";

    private String group;

    private String key = "7e48fd1da37cc5131488c3b37507cf79";

}
