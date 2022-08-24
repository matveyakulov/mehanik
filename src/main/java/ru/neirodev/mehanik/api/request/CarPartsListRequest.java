package ru.neirodev.mehanik.api.request;

import lombok.Data;

@Data
public class CarPartsListRequest extends Request {

    private final String method = "CarPartsList";
    private String typeid;
    private String kid;
    private final String del = ":";
    private String key = "7e48fd1da37cc5131488c3b37507cf79";

}
