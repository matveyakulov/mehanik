package ru.neirodev.mehanik.api.request;

import lombok.Data;

@Data
public class GetMakesRequest extends Request {

    private String group;

    private final String method = "getMakes";

    private String key;

}
