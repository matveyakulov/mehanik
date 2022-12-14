package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Schema(description = "Оценка пользователя другим пользователем")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(schema = "core", name = "users_rating")
@Data
public class UserRatingEntity extends BaseEntity{

    @Schema(description = "Оценка")
    private Double value;

    @Schema(description = "Кому")
    @Column(name = "user_to_id")
    private Long userToId;

    @CreatedBy
    @Schema(description = "От кого (заполню сам)", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "user_from_id")
    private Long userFromId;
}
