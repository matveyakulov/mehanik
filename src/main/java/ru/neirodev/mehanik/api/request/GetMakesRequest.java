package ru.neirodev.mehanik.api.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetMakesRequest extends Request {

    private String group;

    private final String method = "getMakes";
}
