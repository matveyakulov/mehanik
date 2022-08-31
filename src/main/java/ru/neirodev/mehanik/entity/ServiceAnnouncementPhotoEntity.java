package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Schema(description = "Избранное объявление о продаже")
@Entity
@Table(schema = "core", name = "service_announcements_photo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAnnouncementPhotoEntity extends BaseEntity{

    @Schema(description = "id объявления")
    @JoinColumn(name = "service_announcement_id")
    @Column(name = "service_announcement_id")
    private Long serviceAnnouncementId;

    @Schema(description = "id фото")
    private String photo;
}
