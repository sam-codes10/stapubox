package com.example.stapubox.middleware;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.stapubox.models.payloads.JwtClaims;

@Component
public class Auth {
    private final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyzABCDEF"; // move to env later
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(JwtClaims claims) {
        return Jwts.builder()
                .claim("userId", claims.getUserId())
                .claim("email", claims.getEmail())
                .claim("role", claims.getRole())
                .setIssuedAt(claims.getIssuedAt())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtClaims parseToken(String token) throws JwtException {
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

        Claims body = jwsClaims.getBody();
        return new JwtClaims(
                body.get("email", String.class),
                body.get("userId", String.class),
                body.get("role", String.class),
                body.getIssuedAt(),
                body.getExpiration());
    }

}
