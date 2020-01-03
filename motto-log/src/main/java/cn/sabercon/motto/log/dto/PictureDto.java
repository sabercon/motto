package cn.sabercon.motto.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PictureDto {

    Long id;

    String name;

    @ApiModelProperty(value = "文件大小，单位为byte")
    Long size;

    String type;

    String url;

    @ApiModelProperty(value = "缩略图url")
    String thumbnailUrl;

    @ApiModelProperty(value = "删除标记：0-未删除，1-已删除", allowableValues = "0, 1", example = "0")
    Integer del;

    @ApiModelProperty(value = "创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createTime;

}
