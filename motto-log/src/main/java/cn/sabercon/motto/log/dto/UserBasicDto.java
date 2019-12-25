package cn.sabercon.motto.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBasicDto {

    @ApiModelProperty("用户名")
    String username;

    @ApiModelProperty("密码")
    String password;

    @ApiModelProperty("手机号")
    String phone;

    @ApiModelProperty("短信验证码")
    String smsCode;

}
