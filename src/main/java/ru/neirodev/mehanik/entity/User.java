package ru.neirodev.mehanik.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "core", name = "users")
@Data
public class User extends BaseEntity {

    private String name;

    private String site;

    private String phone;

    private String city;

    private String email;

    private Double rating = 0.0;

    @Column(name = "count_rating")
    private Long countRating = 0L;

    @Column(name = "is_company")
    private Boolean isCompany;

    private String photo;
}
