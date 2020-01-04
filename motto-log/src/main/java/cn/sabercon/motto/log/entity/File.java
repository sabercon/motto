package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseEntity;
import cn.sabercon.motto.common.entity.BaseResourceEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

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
@Data
@Where(clause = " del = 0 ")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "userId"))
public class File extends BaseResourceEntity {

    String name;

    @ApiModelProperty("文件大小，单位为byte")
    Long size;

    String type;

    String url;

}
