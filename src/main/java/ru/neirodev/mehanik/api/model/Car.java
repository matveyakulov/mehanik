package ru.neirodev.mehanik.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Модификация модели")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {

    @Schema(description = "id")
    private Long carId;

    @Schema(description = "id модели")
    private Long modelid;

    @Schema(description = "Название")
    private String name;

    @Schema(description = "Дата выпуска (от - до)")
    private String constructioninterval;
}
