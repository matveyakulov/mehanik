package ru.neirodev.mehanik.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Автопроизводитель")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Make {

    @Schema(description = "id")
    private Long makeId;

    @Schema(description = "Название")
    private String makeName;
}
