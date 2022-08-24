package ru.neirodev.mehanik.entity.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.neirodev.mehanik.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "users", name = "roles")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Роль")
public class Role extends BaseEntity {

    @Schema(description = "Название", required = true)
    private String name;
}
