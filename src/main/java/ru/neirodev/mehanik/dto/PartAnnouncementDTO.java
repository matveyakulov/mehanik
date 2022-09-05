package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Schema(description = "Запчасть к модификации машины")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartAnnouncementDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "Тип ТС")
    private String type;

    @Schema(description = "Марка")
    private String brand;

    @Schema(description = "Модель")
    private String model;

    @Schema(description = "Поколение(из модификации что-то сюда пиши)")
    private String generation;

    @Schema(description = "Название запчасти")
    private String nameOfPart;

    @Schema(description = "Номер запчасти")
    private String numberOfPart;

    @Schema(description = "Фото")
    private String photo;

    @Schema(description = "Адрес")
    private String address;

    @Schema(description = "Название города")
    private String city;

    @Schema(description = "Цена")
    private Integer price;

    @Schema(description = "Дата размещения", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateCreate;

    @Schema(description = "Архивность")
    private Boolean archive;

    @Schema(description = "Широта")
    private Double latitude;

    @Schema(description = "Долгота")
    private Double longitude;
}
