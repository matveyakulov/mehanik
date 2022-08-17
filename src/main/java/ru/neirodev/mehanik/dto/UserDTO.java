package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Транспортный обьект пользователя")
@Data
public class UserDTO {

    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Название компании или имя + фамилия человека")
    private String name;

    @Schema(description = "Ссылка на сайт")
    private String site;

    @Schema(description = "Номер телефона")
    private String phone;

    @Schema(description = "Название города")
    private String city;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "true - компания, false - частное лицо")
    private Boolean isCompany;

    @Schema(description = "Идентфикатор фотографии, сохраненной в репозитории")
    private String photo;
}
