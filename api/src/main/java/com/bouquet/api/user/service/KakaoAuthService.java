package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.User;

import java.net.MalformedURLException;

public interface KakaoAuthService {
    //카카오 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    KakaoTokenInfo sendCode(String code) throws Exception;

    //카카오 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    KakaoUserInfo sendToken(String accessToken) throws Exception;

    //사용자 이메일이 DB에 존재하는지 조회해서 회원가입 여부 판단. 존재하면 유저 정보, 존재하지 않으면 null 반환
    User emailCheck(String email);
}