package Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jwt工具类
 */
public class JwtUtils {
    /**
     * 默认的token过期时间，30分钟（60 * 1000=1分钟）
     */
    private static final long DEFAULT_EXPIRATION = 60 * 1000 * 30;

    /**
     * 默认的密钥
     */
    private static final String DEFAULT_SECRET = "java_jwt_secret_key_!@#$%^&*()_+";

    /**
     * 生成Token
     * <p>
     * 使用setExpiration(Date)方式设置过期时间后，调用Claims对象的getExpiration()方法得到的是一个null。
     * 通过查看源码得知，Claims底层维护一个Map，设置的过期时间将以key为"exp"保存在map中。
     * 所以直接通过claim("exp",Date)的方式解决
     *
     * @param expiration 过期时间（单位：秒）
     * @param claims     自定义信息
     * @param secret     密钥
     * @return Token字符串
     */
    public static String generateToken(String secret, long expiration, Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(getSecretKey(secret), SignatureAlgorithm.HS256)
                .setClaims(claims)
                .claim("exp", new Date(System.currentTimeMillis() + expiration))
                .compressWith(CompressionCodecs.GZIP)
                .compact();

    }
    /**
     * 使用默认的过期时间和默认的密钥生成 Token
     *
     * @param claims  自定义信息
     * @return Token 字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return generateToken(DEFAULT_SECRET, DEFAULT_EXPIRATION, claims);
    }

    /**
     * 使用默认的密钥生成 Token
     *
     * @param expiration 过期时间（单位：秒）
     * @param claims     自定义信息
     * @return Token 字符串
     */
    public static String generateToken(long expiration, Map<String, Object> claims) {
        return generateToken(DEFAULT_SECRET, expiration, claims);
    }

    /**
     * 使用默认的过期时间生成 Token
     *
     * @param claims 自定义信息
     * @param secret 密钥
     * @return Token 字符串
     */
    public static String generateToken(Map<String, Object> claims, String secret) {
        return generateToken(secret, DEFAULT_EXPIRATION, claims);
    }

    /**
     * 解析Token
     *
     * @param token  Token字符串
     * @param secret 密钥
     * @return Claims对象
     */
    public static Claims parseToken(String token, String secret) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(secret))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            // 异常原因：token过期
            return null;
        }
        return claimsJws.getBody();
    }

    /**
     * 使用默认的密钥解析 Token
     *
     * @param token Token 字符串
     * @return Claims 对象
     */
    public static Claims parseToken(String token) {
        return parseToken(token, DEFAULT_SECRET);
    }

    /**
     * 判断 Token 是否过期
     */
    public static boolean isExpired(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 根据密钥字符串获取SecretKey对象
     *
     * @param secret 密钥字符串
     * @return SecretKey对象
     */
    private static SecretKey getSecretKey(String secret) {
        byte[] encodedKey = secret.getBytes();
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, SignatureAlgorithm.HS256.getJcaName());
    }
}

