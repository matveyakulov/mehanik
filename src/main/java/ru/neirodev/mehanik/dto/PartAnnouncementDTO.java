package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Schema(description = "Запчасть к модификации машины")
@AllArgsConstructor
@Data
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

    @Schema(description = "Цена")
    private Integer price;

    @Schema(description = "Дата размещения", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateCreate;

    @Schema(description = "Архивность")
    private Boolean archive;
}