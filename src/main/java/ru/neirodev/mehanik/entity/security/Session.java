package ru.neirodev.mehanik.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import ru.neirodev.mehanik.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Where(clause = "not del")
@Table(schema = "users", name = "sessions")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Сессия пользователя")
public class Session extends BaseEntity {

    @JsonIgnore
    @Schema(description = "Метка для удалённых записей", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean del = false;

    @JsonIgnore
    @CreationTimestamp
    @Schema(description = "Дата и время создания записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateCreate;

    @JsonIgnore
    @UpdateTimestamp
    @Schema(description = "Дата и время последнего изменения записи", accessMode = Schema.AccessMode.READ_ONLY)
    private Date dateUpdate;

    @JsonIgnore
    @Column(name = "last_user_id")
    @Schema(description = "Идентификатор пользователя, изменившего запись", accessMode = Schema.AccessMode.READ_ONLY)
    private Long lastUserId;

    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Date lastLogin;
    private String useragent;
    private String userIp;

}
