package com.panaderia.inventario.security;

import com.panaderia.inventario.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String subject) {
        long now = System.currentTimeMillis();
        long accessTokenValidity = jwtProperties.getAccessMinutes() * 60 * 1000;

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenValidity))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Metodo par refrescar token
    public String generateRefreshToken(String subject) {
    long now = System.currentTimeMillis();
    long refreshTokenValidity = jwtProperties.getRefreshDays() * 24 * 60 * 60 * 1000;

    return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + refreshTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
}

//Validar token
public boolean isValid(String token){
    try {
        Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);
        return true;
    } catch (Exception e) {
        return false;
    }
}
public String extractSubject(String token){
    return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}

}
