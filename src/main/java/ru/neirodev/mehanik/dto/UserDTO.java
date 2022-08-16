package ru.neirodev.mehanik.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String name;

    private String site;

    private String phone;

    private String city;

    private String email;

    private Boolean isCompany;

    private String photo;
}
