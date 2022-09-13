package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neirodev.mehanik.enums.CarType;

import javax.persistence.*;

@Schema(description = "Избранное объявление о продаже")
@Entity
@Table(schema = "core", name = "service_announcements_car_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAnnouncementCarTypeEntity extends BaseEntity{

    @Schema(description = "id объявления")
    @Column(name = "service_announcement_id")
    private Long serviceAnnouncementId;

    @Schema(description = "Тип ТС")
    @Enumerated(EnumType.STRING)
    private CarType type;
}
