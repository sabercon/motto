package cn.sabercon.motto.log.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailDto {

    @ApiModelProperty("昵称")
    String nickname;

    @ApiModelProperty("个人简介")
    String profile;

    @ApiModelProperty("头像链接")
    String avatar;

    @ApiModelProperty(value = "性别：1-男，2-女", allowableValues = "1, 2", example = "1")
    Integer gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @ApiModelProperty("地址，格式为：国家-省份-城市")
    String address;

}
