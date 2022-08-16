package ru.neirodev.mehanik.dto;

import lombok.Data;

@Data
public class SetFieldRequest {

    private Long id;

    private String fieldName;

    private Object fieldValue;

}
