package ru.neirodev.mehanik.api.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VinDecodeShortRequest extends Request {

    private final String method = "VINdecodeShort";
    private String vin;
}
