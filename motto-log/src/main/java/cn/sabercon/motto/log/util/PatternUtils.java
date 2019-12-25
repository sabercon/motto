package cn.sabercon.motto.log.util;

import cn.sabercon.motto.common.util.AssertUtils;
import lombok.experimental.UtilityClass;

/**
 * 用正则表达式校验参数的工具类
 *
 * @author ywk
 * @date 2019-10-31
 */
public class PatternUtils {
    /**
     * 校验手机号（11位数字）
     */
    private static final String REGEX_USER_PHONE = "^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\\d{8}$";
    /**
     * 校验短信验证码（6位数字）
     */
    private static final String REGEX_USER_SMS_CODE = "^\\d{6}$";
    /**
     * 校验用户名（2-40位字符，可为字母或数字或下划线）
     */
    private static final String REGEX_USER_USERNAME = "^[A-Za-z0-9_]{2,40}$";
    /**
     * 校验密码（6-20位字符，可为字母或数字）
     */
    private static final String REGEX_USER_PASSWORD = "^[A-Za-z0-9]{6,20}$";

    public static void checkUsername(String username) {
        AssertUtils.matchPattern(REGEX_USER_USERNAME, username);
    }

    public static void checkPassword(String password) {
        AssertUtils.matchPattern(REGEX_USER_PASSWORD, password);
    }

    public static void checkPhone(String phone) {
        AssertUtils.matchPattern(REGEX_USER_PHONE, phone);
    }

    public static void checkSmsCode(String smsCode) {
        AssertUtils.matchPattern(REGEX_USER_SMS_CODE, smsCode);
    }

}
