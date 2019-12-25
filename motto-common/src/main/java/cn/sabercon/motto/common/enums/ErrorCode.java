package cn.sabercon.motto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * 自定义的错误码
 *
 * @author ywk
 * @date 2019-10-15
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 通用操作码
    FAIL(50000,"fail"),
    UNAUTHORIZED(40001, "unauthorized user"),
    // 用户相关
    SMS_SEND_ERROR(40011, "error when sending sms code"),
    SMS_CODE_WRONG(40012, "sms code is wrong"),
    USERNAME_EXISTS(40013, "username is already used"),
    PHONE_EXISTS(40014, "phone number is already bound"),
    USER_NOT_EXISTS(40015, "user does not exist"),
    PASSWORD_WRONG(40016, "password does not match"),
    PHONE_BIND_STATUS_WRONG(40017, "user is not phone-changeable status"),
    ;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;
}
