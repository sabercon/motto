package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseEntity;
import cn.sabercon.motto.common.entity.BaseResourceEntity;
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
@Data
@Entity
@Where(clause = " del = 0 ")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "userId"))
public class Diary extends BaseResourceEntity {

    String name;

    String type;

    String note;

    @Column(columnDefinition = "text")
    String text;

}
