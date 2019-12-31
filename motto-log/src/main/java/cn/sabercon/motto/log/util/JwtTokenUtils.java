package cn.sabercon.motto.log.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 *
 * @author ywk
 * @date 2019-10-31
 */
@Slf4j
public class JwtTokenUtils {

    /**
     * JWT加密的密钥
     */
    private static final String JWT_SECRET = "mySecret";
    /**
     * JWT默认过期时间（一个月，60*60*24*30）
     */
    public static final Long JWT_USER_EXPIRATION = 2592000L;
    /**
     * 登录用的 cookie名称
     */
    public static final String JWT_COOKIE_NAME = "JWT_TOKEN";
    /**
     * redis中缓存账号信息的键名前缀
     */
    public static final String JWT_USER_PREFIX = "user:cache:";
    /**
     * 登录过滤器加在 request域中的用户 id的属性名
     */
    public static final String JWT_USER_ID = "userId";

    /**
     * 根据负责生成 JWT的 token
     */
    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_USER_EXPIRATION * 1000))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    /**
     * 根据用户 id生成 token
     */
    public static String generateTokenById(Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.ID, id);
        return generateToken(claims);
    }

    /**
     * 从 token中获取 JWT中的负载，失败时返回 null
     */
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.warn("JWT format error, token:{}", token);
            return null;
        }
    }

    /**
     * 从 token中获取登录用户 id，失败时返回 null
     */
    public static String getIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims == null ? null : claims.getId();
    }

    /**
     * 判断 token是否已经失效
     */
    public static boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return true;
        }
        Date expiredDate = claims.getExpiration();
        return expiredDate.before(new Date());
    }

    public static boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新 token
     */
    public static String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return generateToken(claims);
    }
}
