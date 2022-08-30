package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Date;

@Schema(description = "Объявление о продаже запчасти")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(schema = "core", name = "parts_announcement")
@Data
public class PartAnnouncementEntity extends BaseEntity{

    @Schema(description = "Тип ТС")
    private String type;

    @Schema(description = "Марка")
    private String brand;

    @Schema(description = "Модель")
    private String model;

    @Schema(description = "Поколение(из модификации что-то сюда пиши)")
    private String generation;

    @Schema(description = "Название запчасти")
    private String nameOfPart;

    @Schema(description = "Номер запчасти")
    private String numberOfPart;

    @Schema(description = "Состояние: true - новая")
    private Boolean condition;

    @Schema(description = "true - ориг")
    private Boolean original;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Цена")
    private Integer price;

    @Schema(description = "Адрес")
    private String address;

    @Schema(description = "Фото")
    private String photo;

    @Schema(description = "Дата размещения", accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedDate
    private Date dateCreate;

    @Schema(description = "true - разрешить писать на почту", defaultValue = "false")
    private Boolean useEmail = false;

    @Schema(description = "true - разрешить звонить / писать на мобилу", defaultValue = "false")
    private Boolean usePhone = false;

    @Schema(description = "true - разрешить звонить / писать на вотсап", defaultValue = "false")
    private Boolean use_whatsapp = false;

    @Schema(description = "Архивность", defaultValue = "false")
    private Boolean archive = false;

    @Schema(description = "Владелец объявления", accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedBy
    @JoinColumn(name = "owner_id", updatable = false)
    private Long ownerId;
}
