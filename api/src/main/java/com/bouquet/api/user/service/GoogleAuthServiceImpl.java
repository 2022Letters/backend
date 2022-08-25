package com.bouquet.api.user.service;

import com.bouquet.api.user.dto.GoogleTokenInfo;
import com.bouquet.api.user.dto.GoogleUserInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String googleRedirectUri;

    //구글 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    @Override
    public GoogleTokenInfo sendCode(String code) throws Exception {
        System.out.println("sendCode");

        String reqUrl = "https://oauth2.googleapis.com/token";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("client_id=" + googleClientId);
        sb.append("&client_secret=" + googleClientSecret);
        sb.append("&code=" + code);
        sb.append("&grant_type=authorization_code");
        sb.append("&redirect_uri=" + googleRedirectUri);

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

        GoogleTokenInfo googleTokenInfo = om.readValue(result, GoogleTokenInfo.class);

        System.out.println(googleTokenInfo.toString());

        return googleTokenInfo;
    }

    //구글 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    @Override
    public GoogleUserInfo sendToken(String accessToken) throws Exception {
        System.out.println("sendToken");
        String reqUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
//        String reqUrl = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses";
        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

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

        GoogleUserInfo googleUserInfo = om.readValue(result, GoogleUserInfo.class);

        System.out.println(googleUserInfo.toString());

        return googleUserInfo;
    }

    //사용자 소셜id가 DB에 존재하는지 조회해서 회원가입 여부 판단. 존재하면 유저 정보, 존재하지 않으면 null 반환
    @Override
    public User socialIdCheck(String socialId) {
        User user = userRepository.findByGoogleSocialId(socialId);
        return user;
    }
}
