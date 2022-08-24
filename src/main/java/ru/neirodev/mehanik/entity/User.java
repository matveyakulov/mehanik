package ru.neirodev.mehanik.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.neirodev.mehanik.entity.security.Role;

import javax.persistence.*;

@Schema(description = "Пользователь")
@Entity
@Table(schema = "core", name = "users")
@Data
public class User extends BaseEntity {

    @Schema(description = "Название компании или имя + фамилия человека")
    private String name;

    @Schema(description = "Ссылка на сайт")
    private String site;

    @Schema(description = "Номер телефона")
    private String phone;

    @Schema(description = "Название города")
    private String city;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Рейтинг", accessMode = Schema.AccessMode.READ_ONLY)
    private Double rating = 0.0;

    @Schema(description = "true - компания, false - частное лицо")
    @Column(name = "is_company")
    private Boolean isCompany;

    @Schema(description = "Идентфикатор фотографии, сохраненной в репозитории")
    private String photo;

    @JsonIgnore
    private String smscode;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}
