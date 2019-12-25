package cn.sabercon.motto.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

/**
 * 添加cookie的工具类
 *
 * @author ywk
 * @date 2019-10-15
 */
@UtilityClass
public class CookieUtils {

    public void addCookie(Cookie cookie) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        assert attributes.getResponse() != null;
        attributes.getResponse().addCookie(cookie);
    }

    public void addCookie(String name, String value, int expiry, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        addCookie(cookie);
    }

    /**
     * 默认路径为根路径
     */
    public void addCookie(String name, String value, int expiry) {
        addCookie(name, value, expiry, "/");
    }

    /**
     * 默认过期时间为-1，路径为根路径
     */
    public void addCookie(String name, String value) {
        addCookie(name, value, -1);
    }
}
