package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Запчасть к модификации машины")
@AllArgsConstructor
@Data
public class ServiceAnnouncementDTO {

    @Schema(description = "Уникальный идентификатор записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Название сервиса")
    private String companyName;

    @Schema(description = "Полный адрес")
    private String address;

    @Schema(description = "Рейтинг")
    private Double rating;
}
