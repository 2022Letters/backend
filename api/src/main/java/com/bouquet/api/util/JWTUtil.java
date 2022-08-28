package com.bouquet.api.util;

import com.bouquet.api.user.exception.NotValidateAccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtil {
    // 암호화 키 설정
    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    // accessToken 유효시간 설정
    private long tokenValidTime = 1000L * 60 * 60; // 60분
    // refreshToken 유효시간 설정
    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7일

    // 초기 실행되는 함수, Jwt 생성 시 서명으로 사용할 secretKey Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // accessToken 생성
    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    // refreshToken 생성
    public String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    // 토큰 유효성 확인
    public boolean validateTokenExpiration(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // accessToken 생성시 사용한 user의 id 값 반환
    public String getUserId(String accesstoken) throws NotValidateAccessToken {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accesstoken).getBody().getSubject();
        } catch(Exception e) {
            throw new NotValidateAccessToken();
        }
    }
}