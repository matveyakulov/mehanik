package ru.neirodev.mehanik.api.model;

import lombok.Data;

@Data
public class GetMakesRequest extends Request {

    private String group;

    private final String method = "getMakes";

    //@Value("${partsapi.getMakes.key}")
    private String key = "48E0D8C38B230FFC60FC029E83AA5E61";

}
