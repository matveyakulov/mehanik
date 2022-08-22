package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Schema(description = "Оценка пользователя другим пользователем")
@Entity
@Table(schema = "core", name = "users_rating")
@Data
public class UserRating extends BaseEntity{

    private Double value;

    @JoinColumn(name = "user_to_id")
    private Long userToId;

    @JoinColumn(name = "user_from_id")
    private Long userFromId;
}
