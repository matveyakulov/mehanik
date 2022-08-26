package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Schema(description = "Избранное объявление о продаже")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(schema = "core", name = "parts_announcement_favorites")
@Data
public class PartAnnouncementFavoriteEntity extends BaseEntity{

    @Schema(description = "id пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    @JoinColumn(name = "user_id", updatable = false)
    private Long userId;

    @CreatedBy
    @Schema(description = "id объявления о продаже")
    @JoinColumn(name = "parts_announcement_id", updatable = false)
    private Long partsAnnouncementId;
}
