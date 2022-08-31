package ru.neirodev.mehanik.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.neirodev.mehanik.enums.ServiceType;

import javax.persistence.*;
import java.util.Set;

@Schema(description = "Избранное объявление о продаже")
@Entity
@Table(schema = "core", name = "service_announcements")
@Data
public class ServiceAnnouncementEntity extends BaseEntity{

    @Schema(description = "Тип сервиса")
    @Enumerated(EnumType.STRING)
    private ServiceType service;

    @Schema(description = "Название сервиса")
    private String companyName;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Полный адрес")
    private String address;

    @Schema(description = "Широта")
    private Double latitude;

    @Schema(description = "Долгота")
    private Double longitude;

    @Schema(description = "Рейтинг")
    private Double rating;

    @Schema(description = "Типы ТС", accessMode = Schema.AccessMode.READ_ONLY)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(schema = "core", name = "service_announcements_car_type", joinColumns = @JoinColumn(name = "service_announcement_id"))
    @Column(name = "type")
    private Set<String> carTypes;

    @Schema(description = "Марки ТС", accessMode = Schema.AccessMode.READ_ONLY)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(schema = "core", name = "service_announcements_car_brand", joinColumns = @JoinColumn(name = "service_announcement_id"))
    @Column(name = "brand")
    private Set<String> brands;

    @Schema(description = "Фотки", accessMode = Schema.AccessMode.READ_ONLY)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(schema = "core", name = "service_announcements_photo", joinColumns = @JoinColumn(name = "service_announcement_id"))
    @Column(name = "photo")
    private Set<String> photos;
}
