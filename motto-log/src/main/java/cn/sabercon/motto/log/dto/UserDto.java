package cn.sabercon.motto.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @ApiModelProperty("用户名")
    String username;

    @ApiModelProperty("手机号")
    String phone;

    @ApiModelProperty("昵称")
    String nickname;

    @ApiModelProperty("个人简介")
    String profile;

    @ApiModelProperty("头像链接")
    String avatar;

    @ApiModelProperty(value = "性别：1-男，2-女", allowableValues = "1, 2", example = "1")
    Integer gender;

    @ApiModelProperty("出生日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    String country;

    String province;

    String city;

}
