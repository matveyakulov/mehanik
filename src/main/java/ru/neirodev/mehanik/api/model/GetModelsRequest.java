package ru.neirodev.mehanik.api.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class GetModelsRequest extends Request {

    private Long make;

    private final String method = "getModels";

    private String group;

    @Value("${partsapi.getModels.key}")
    private String key;

}
