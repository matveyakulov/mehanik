package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Schema(description = "Оценка пользователя другим пользователем")
@Entity
@Table(schema = "core", name = "users_rating")
@Data
public class UserRatingEntity extends BaseEntity{

    @Schema(description = "Оценка")
    private Double value;

    @Schema(description = "Кому")
    @JoinColumn(name = "user_to_id")
    private Long userToId;

    @Schema(description = "От кого (заполню сам)", accessMode = Schema.AccessMode.READ_ONLY)
    @JoinColumn(name = "user_from_id")
    private Long userFromId;

    @PrePersist
    private void save(){
        userFromId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
