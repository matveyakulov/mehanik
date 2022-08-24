package ru.neirodev.mehanik.dto;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class SmsDTO {

    private String phone;
    private String code;

    private String secret;

    public SmsDTO(String phone, String secret) {
        this.phone = phone;
        this.secret = secret;
        code = RandomStringUtils.randomNumeric(4);
    }
}
