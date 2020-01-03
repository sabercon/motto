package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Entity
@Table(indexes = @Index(columnList = "userId"))
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Picture extends BaseEntity {

    @Column(nullable = false, unique = true)
    Long userId;

    String name;

    Long size;

    String type;

    String url;

    String thumbnailUrl;

    Integer del;

}
