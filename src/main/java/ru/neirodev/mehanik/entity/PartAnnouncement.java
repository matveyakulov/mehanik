package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Date;

@Schema(description = "Объявление о продаже запчасти")
@Entity
@Table(schema = "core", name = "parts_announcement")
@Data
public class PartAnnouncement extends BaseEntity{

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
    @CreationTimestamp
    private Date dateCreate;

    @Schema(description = "true - разрешить писать на почту", defaultValue = "false")
    private Boolean useEmail = false;

    @Schema(description = "true - разрешить звонить / писать на мобилу", defaultValue = "false")
    private Boolean usePhone = false;

    @Schema(description = "true - разрешить звонить / писать на вотсап", defaultValue = "false")
    private Boolean use_whatsapp = false;

    @Schema(description = "Архивность", defaultValue = "false")
    private Boolean archive = false;

    @Schema(description = "Владелец объявления")
    @JoinColumn(name = "owner_id", updatable = false)
    private Long ownerId;

    @PrePersist
    private void save(){
        ownerId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
