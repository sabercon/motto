package cn.sabercon.motto.log.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 获取登录信息的工具类
 *
 * @author ywk
 * @date 2019-11-05
 */
public class LoginUtils {

    /**
     * 获取登录用户的 id
     */
    public static Long getId() {
        String id = (String) RequestContextHolder.currentRequestAttributes().getAttribute(JwtTokenUtils.JWT_USER_ID, RequestAttributes.SCOPE_REQUEST);
        assert id != null;
        return Long.parseLong(id);
    }
}
