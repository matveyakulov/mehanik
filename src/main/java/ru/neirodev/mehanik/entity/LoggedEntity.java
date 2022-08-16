package ru.neirodev.mehanik.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder(builderMethodName = "", toBuilder = true)
@NoArgsConstructor
public abstract class LoggedEntity extends BaseEntity {

    @CreatedBy
    @JsonIgnore
    @Schema(description = "Идентификатор пользователя, создавшего запись", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "id_usersadded", updatable = false)
    private Long idStaffsAdded;

    @CreatedDate
    @JsonIgnore
    @Schema(description = "Дата и время создания", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "datetimeadded", updatable = false)
    private Date datetimeAdded;

    @LastModifiedBy
    @JsonIgnore
    @Schema(description = "Идентификатор последнего изменявшего запись пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "id_staffschanged")
    private Long idStaffsChanged;

    @LastModifiedDate
    @JsonIgnore
    @Schema(description = "Дата и время последнего изменения", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "datetimechanged")
    private Date datetimeChanged;

}