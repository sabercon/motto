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
 * @date 2019-10-15
 */
@Entity
@Table(indexes = @Index(columnList = "userId"))
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetail extends BaseEntity {

    @Column(nullable = false, unique = true)
    Long userId;

    String nickname;

    String profile;

    String avatar;

    Integer gender;

    LocalDate birthday;

    String country;

    String province;

    String city;

}
