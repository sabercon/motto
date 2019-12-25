package cn.sabercon.motto.log.component;

import cn.hutool.core.map.MapUtil;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.log.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

import static cn.sabercon.motto.common.enums.ErrorCode.UNAUTHORIZED;

/**
 * 判断是否登录的过滤器
 *
 * @author ywk
 * @date 2019-10-31
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 校验用户是否登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跨域预请求放行
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 获取token
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> JwtTokenUtils.JWT_COOKIE_NAME.equalsIgnoreCase(cookie.getName()))
                .findFirst().map(Cookie::getValue).orElse(null);
        AssertUtils.isNotNull(token, UNAUTHORIZED);
        // 获取id
        String id = JwtTokenUtils.getIdFromToken(token);
        AssertUtils.isNotNull(id, UNAUTHORIZED);
        // 判断token是否过期
        AssertUtils.isTrue(JwtTokenUtils.isTokenValid(token), UNAUTHORIZED);
        // 判断redis中是否有相关的用户缓存信息
        String key = JwtTokenUtils.JWT_USER_PREFIX + id;
        AssertUtils.isTrue(redisTemplate.hasKey(key), UNAUTHORIZED);
        // 保存用户id到request域中
        request.setAttribute(JwtTokenUtils.JWT_USER_ID, id);
        return true;
    }
}
