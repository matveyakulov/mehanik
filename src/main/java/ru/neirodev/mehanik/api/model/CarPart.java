package ru.neirodev.mehanik.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Запчасть к модификации машины")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarPart {

    @Schema(description = "Название запчасти")
    private String name;

    @Schema(description = "Ее варианты")
    private List<Part> parts = new ArrayList<>();
}
