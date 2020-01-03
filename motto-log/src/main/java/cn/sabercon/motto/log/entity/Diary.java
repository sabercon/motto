package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

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
@Where(clause = " del = 0 ")
public class Diary extends BaseEntity {

    @Column(nullable = false, unique = true)
    Long userId;

    String name;

    String type;

    @Column(columnDefinition = "text")
    String text;

    Integer del;

}
