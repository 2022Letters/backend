package com.bouquet.api.user.exception;

public class NotValidateAccessToken extends Exception{
    public NotValidateAccessToken() {
        super("유효하지 않은 AccessToken 입니다.");
    }
}
