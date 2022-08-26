package ru.neirodev.mehanik.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Расшифровка VIN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VinDecode {

    @Schema(description = "id модификации")
    private Long carId;

    @Schema(description = "Тип ТС (сокращенный)")
    private String linkageTargetType;

    @Schema(description = "id марки")
    private Long manuId;

    @Schema(description = "id модели")
    private Long modId;
}
