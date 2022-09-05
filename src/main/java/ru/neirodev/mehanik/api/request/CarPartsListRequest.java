package ru.neirodev.mehanik.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarPartsListRequest extends Request {

    private String typeid;
    private String kid;
    private final String method = "CarPartsList";
    private final String del = ":";
}
