package ru.neirodev.mehanik.api.request;

import lombok.Data;

@Data
public class CarPartsListRequest extends Request {

    private final String method = "CarPartsList";
    private String typeid;
    private String kid;
    private final String del = ":";
    private String key;

}
