package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.RefreshedTokenInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {
    @Autowired
    private UserRepository userRepository;

    //카카오 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    @Override
    public KakaoTokenInfo sendCode(String code) throws Exception {
        System.out.println("sendCode");
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        KakaoTokenInfo kakaoTokenInfo = null;

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=644f22ddf456df96ce3ac3e3b870991a");
        sb.append("&redirect_uri=http://localhost:3000/login/redirect");
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper om = new ObjectMapper();

        kakaoTokenInfo = om.readValue(result, KakaoTokenInfo.class);

        System.out.println(kakaoTokenInfo.toString());

        return kakaoTokenInfo;
    }

    //카카오 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    @Override
    public KakaoUserInfo sendToken(String accessToken) throws Exception {
        System.out.println("sendToken");
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper om = new ObjectMapper();

        KakaoUserInfo kakaoUserInfo = om.readValue(result, KakaoUserInfo.class);

        System.out.println(kakaoUserInfo.toString());

        return kakaoUserInfo;
    }

    //사용자 이메일이 DB에 존재하는지 조회해서 회원가입 여부 판단. 존재하면 유저 정보, 존재하지 않으면 null 반환
    @Override
    public User emailCheck(String email) {
        User userNickname = userRepository.existsByEmail(email);
        if (userNickname != null) {
            System.out.println("이메일이 존재합니다.");
        } else {
            System.out.println("이메일이 존재하지 않습니다.");
        }
        return userNickname;
    }

    //유저 아이디를 받아 토큰을 갱신하고 액세스 토큰 반환
    @Override
    public String refreshToken(Long userId) throws Exception {
        System.out.println("refreshToken");

        String reqUrl = "https://kauth.kakao.com/oauth/token";

        String refreshToken = userRepository.findById(userId).get().getKakaoRefreshToken();
        System.out.println(refreshToken);

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=refresh_token");
        sb.append("&client_id=644f22ddf456df96ce3ac3e3b870991a");
        sb.append("&refresh_token=" + refreshToken);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper om = new ObjectMapper();

        RefreshedTokenInfo refreshedTokenInfo = om.readValue(result, RefreshedTokenInfo.class);

        System.out.println(refreshedTokenInfo.toString());

        return refreshedTokenInfo.getAccessToken();
    }

    //액세스 토큰으로 카카오 로그아웃
    @Override
    public void kakaoUnlink(String accessToken) throws Exception {
        System.out.println("kakaoUnlink");

        String reqUrl = "https://kapi.kakao.com/v1/user/unlink";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);
    }
}