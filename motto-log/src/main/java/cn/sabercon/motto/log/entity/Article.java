package cn.sabercon.motto.log.entity;

import cn.sabercon.motto.common.entity.BaseResourceEntity;
import io.swagger.annotations.ApiModelProperty;
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
public class Article extends BaseResourceEntity {

    String title;

    @ApiModelProperty(value = "文章类型：html或者markdown", allowableValues = "html, markdown")
    String type;

    String note;

    @Column(columnDefinition = "longtext")
    @ApiModelProperty("markdown文本或用于展示的html文本")
    String text;

    @Column(columnDefinition = "longtext")
    @ApiModelProperty("BraftEditor需要使用的储存数据")
    String rawJson;

}
