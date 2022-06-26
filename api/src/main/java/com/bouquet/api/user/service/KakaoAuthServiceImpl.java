package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {

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
        sb.append("&redirect_uri=http://localhost:8080/oauth");
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

    @Override
    public void sendToken(String accessToken) throws Exception {
        System.out.println("sendToken");
        String reqUrl = "https://kapi.kakao.com/v2/user/me";


        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//            String[] propertyKeys = {"kakao_account.name", "kakao_account.email"};
//            ObjectMapper om = new ObjectMapper();
//            String Json = om.writeValueAsString(propertyKeys);
//            System.out.println(Json);
//            sb.append("property_keys=" + Json);
//            bw.write(sb.toString());
//            bw.flush();

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

    }
}