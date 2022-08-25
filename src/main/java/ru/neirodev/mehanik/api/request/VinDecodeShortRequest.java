package ru.neirodev.mehanik.api.request;

import lombok.Setter;

@Setter
public class VinDecodeShortRequest extends Request {

    private final String method = "VINdecodeShort";
    private String vin;
    private String key;
}
