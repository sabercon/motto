package cn.sabercon.motto.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiaryDto {

    Long id;

    String name;

    @ApiModelProperty("日记类型")
    String type;

    @ApiModelProperty("日记内容")
    String text;

    @ApiModelProperty(value = "删除标记：0-未删除，1-已删除", allowableValues = "0, 1", example = "0")
    Integer del;

    @ApiModelProperty("创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;

    @ApiModelProperty("更新日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;

}
