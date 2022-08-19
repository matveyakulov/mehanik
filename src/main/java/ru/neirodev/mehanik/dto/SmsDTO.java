package ru.neirodev.mehanik.dto;

import lombok.Data;

@Data
public class SmsDTO {

    private String phone;
    private String message;
    private String secret;
}
