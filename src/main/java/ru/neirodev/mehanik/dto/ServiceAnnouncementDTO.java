package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.neirodev.mehanik.enums.ServiceType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Schema(description = "Транспортный объект объявления сервиса")
@AllArgsConstructor
@Data
public class ServiceAnnouncementDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "Тип сервиса")
    @Enumerated(EnumType.STRING)
    private ServiceType service;

    @Schema(description = "Название сервиса")
    private String companyName;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Полный адрес")
    private String address;

    @Schema(description = "Широта")
    private Double latitude;
}
