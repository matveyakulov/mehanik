package ru.neirodev.mehanik.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Объект для фильтрации")
@Data
public class FilterDTO {

    private String city;

    private List<String> types;

    private List<String> brands;

    private String nameOfPart;

    private Integer startPrice;
    private Integer endPrice;

    private Boolean condition;

    private Boolean original;

    private Boolean isCompany;

    @Schema(description = "Номер страницы(с 0)")
    private Integer pageNum;

    private Integer pageSize;
}
