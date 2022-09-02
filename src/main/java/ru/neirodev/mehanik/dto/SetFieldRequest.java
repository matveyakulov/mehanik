package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Объект для изменения одного простого поля(примитивного) у любого объекта")
@Data
public class SetFieldRequest {

    @Schema(description = "Идентификатор изменяемого обьекта")
    private Long id;

    @Schema(description = "Название поля")
    private String fieldName;

    @Schema(description = "Новое значение поля")
    private Object fieldValue;

}
