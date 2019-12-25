package cn.sabercon.motto.common.util;

import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.exception.CommonException;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 断言校验的工具类，不符合时抛出 {@link CommonException}
 *
 * @author ywk
 * @date 2019-10-15
 */
@UtilityClass
public class AssertUtils {

    public void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new CommonException(errorCode);
        }
    }

    public void isTrue(boolean expression) {
        isTrue(expression, ErrorCode.FAIL);
    }

    public void isNull(Object object, ErrorCode errorCode) {
        isTrue(object == null, errorCode);
    }

    public void isNull(Object object) {
        isNull(object, ErrorCode.FAIL);
    }

    public void isNotNull(Object object, ErrorCode errorCode) {
        isTrue(object != null, errorCode);
    }

    public void isNotNull(Object object) {
        isNotNull(object, ErrorCode.FAIL);
    }

    public void isNotEmpty(String string, ErrorCode errorCode) {
        isTrue(StringUtils.hasLength(string), errorCode);
    }

    public void isNotEmpty(String string) {
        isNotEmpty(string, ErrorCode.FAIL);
    }

    public void isNotEmpty(Collection collection, ErrorCode errorCode) {
        isTrue(!CollectionUtils.isEmpty(collection), errorCode);
    }

    public void isNotEmpty(Collection collection) {
        isNotEmpty(collection, ErrorCode.FAIL);
    }

    public void isNotEmpty(Map map, ErrorCode errorCode) {
        isTrue(!CollectionUtils.isEmpty(map), errorCode);
    }

    public void isNotEmpty(Map map) {
        isNotEmpty(map, ErrorCode.FAIL);
    }

    /**
     * 判断字符串与正则表达式是否匹配，input为null时直接抛异常
     *
     * @param regex     校验的正则表达式
     * @param input     要匹配的字符串
     * @param errorCode 错误码
     */
    public void matchPattern(String regex, String input, ErrorCode errorCode) {
        isTrue(input != null && Pattern.matches(regex, input), errorCode);
    }

    public void matchPattern(String regex, String input) {
        matchPattern(regex, input, ErrorCode.FAIL);
    }
}
