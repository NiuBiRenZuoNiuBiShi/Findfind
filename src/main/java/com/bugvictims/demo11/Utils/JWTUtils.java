package com.bugvictims.demo11.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWTUtils {
    private static final String SECRET_KEY = "find-your-dazi-and-this-is-the-secret-key-and-the-now-is-just-for-add-length";
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7 * 1000;

    public static String generateToken(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .claims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
        return jwt;
    }

    public static Claims validateToken(String token) {
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
