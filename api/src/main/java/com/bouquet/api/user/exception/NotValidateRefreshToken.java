package com.bouquet.api.user.exception;

public class NotValidateRefreshToken extends Exception{
    public NotValidateRefreshToken() {
        super("유효하지 않은 refreshToken 입니다.");
    }
}