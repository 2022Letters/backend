package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.KakaoTokenInfo;

import java.net.MalformedURLException;

public interface KakaoAuthService {
    KakaoTokenInfo sendCode(String code) throws Exception;

    void sendToken(String accessToken) throws Exception;
}
