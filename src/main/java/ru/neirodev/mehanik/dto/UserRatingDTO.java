package ru.neirodev.mehanik.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRatingDTO {

    private double count;

    private Long sum;
}
