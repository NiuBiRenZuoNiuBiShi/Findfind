package com.bugvictims.demo11.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWTUtils {
    private static final String SECRET_KEY = "find-your-dazi-and-this-is-the-secret-key-and-the-now-is-just-for-add-length";
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7 * 1000; // 时间 24hrs

    public static String generateToken(Map<String, Object> claims) { // 登录成功后，返回JWT令牌
        String jwt = Jwts.builder() // claims 是 token里可以携带的信息
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
        return jwt;
    }

    public static Claims validateToken(String token) { // 检测JWT令牌是否有效，并返回JWT令牌所携带的信息
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }


}
