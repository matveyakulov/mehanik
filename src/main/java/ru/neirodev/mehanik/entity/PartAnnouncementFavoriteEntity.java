package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Schema(description = "Избранное объявление о продаже")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(schema = "core", name = "parts_announcement_favorites")
@Data
public class PartAnnouncementFavoriteEntity extends BaseEntity{

    @CreatedBy
    @Schema(description = "id пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Schema(description = "id объявления о продаже")
    @Column(name = "parts_announcement_id", updatable = false)
    private Long partsAnnouncementId;
}
