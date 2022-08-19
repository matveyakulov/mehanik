package ru.neirodev.mehanik.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    private Long carId;

    private Long modelId;

    private String name;

    private String constructioninterval;


}
