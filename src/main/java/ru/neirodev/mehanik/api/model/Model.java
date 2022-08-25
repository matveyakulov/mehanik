package ru.neirodev.mehanik.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Модель машины")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Model {

    @Schema(description = "id")
    private Long modelId;

    @Schema(description = "Название")
    private String modelName;
}
