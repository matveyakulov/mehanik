package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Schema(description = "ТС")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(schema = "core", name = "users_cars")
@Data
public class CarEntity extends BaseEntity {

    @Schema(description = "VIN-код")
    private String vin;

    @Schema(description = "Тип")
    private String type;

    @Schema(description = "Марка")
    private String brand;

    @Schema(description = "Модель")
    private String model;

    @Schema(description = "Года выпуска")
    private String release;

    @Schema(description = "Фото")
    private String photo;

    @Schema(description = "id владельца", accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedBy
    @JoinColumn(name = "user_id")
    private Long userId;
}
