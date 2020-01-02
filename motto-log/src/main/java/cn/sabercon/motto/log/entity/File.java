package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @author ywk
 * @date 2020-01-02
 */
@Entity
@Table(indexes = @Index(columnList = "userId"))
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class File extends BaseEntity {

    @Column(nullable = false, unique = true)
    Long userId;

    String name;

    Long size;

    String type;

    String url;

    Integer del;

}
