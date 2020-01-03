package cn.sabercon.motto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    FAIL(50000, "网络异常"),
    UNAUTHORIZED(40001, "用户未登录或登录信息过期"),
    UPLOAD_FAIL(40002, "网络异常，上传文件失败"),
    // 用户相关
    SMS_SENDING_ERROR(40011, "短信验证码发送失败"),
    SMS_CODE_WRONG(40012, "短信验证码错误"),
    USERNAME_EXISTS(40013, "用户名已存在"),
    PHONE_EXISTS(40014, "手机号已被绑定"),
    USER_NOT_EXISTS(40015, "用户不存在"),
    PASSWORD_WRONG(40016, "密码错误"),
    PHONE_BIND_WRONG(40017, "原手机号未解绑，请先解绑"),
    // 资源相关
    PIC_TYPE_WRONG(40021,"图片类型错误")
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
