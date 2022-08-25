package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.GoogleTokenInfo;
import com.bouquet.api.user.dto.GoogleUserInfo;
import com.bouquet.api.user.dto.User;

public interface GoogleAuthService {
    //구글 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    GoogleTokenInfo sendCode(String code) throws Exception;

    //구글 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    GoogleUserInfo sendToken(String accessToken) throws Exception;

    //사용자 소셜id가 DB에 존재하는지 조회해서 회원가입 여부 판단. 존재하면 유저 정보, 존재하지 않으면 null 반환
    User socialIdCheck(String socialId);
}
