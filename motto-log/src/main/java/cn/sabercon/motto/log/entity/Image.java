package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseResourceEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Entity
@Data
@Where(clause = " del = 0 ")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "userId"))
public class Image extends BaseResourceEntity {

    String name;

    @ApiModelProperty("图片大小，单位为byte")
    Long size;

    String type;

    String url;

    @ApiModelProperty("缩略图路径")
    String thumbnailUrl;

}
