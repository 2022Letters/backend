package com.bouquet.api.user.web;


import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.user.service.KakaoAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

@RestController
public class KakaoAuthController {
    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private UserRepository userRepository;

    //프론트에서 인증 코드를 보내주면 카카오 서버와 통신하고 사용자 정보를 조회한다.
    @RequestMapping("kakaoLogin")
    public ResponseEntity<Object> kakaoLogin(String code) {
        System.out.print(code);
        try {
            KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
            KakaoUserInfo kakaoUserInfo = kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());
            User userInfo = kakaoAuthService.emailCheck(kakaoUserInfo.getEmail());

            HashMap<String, String> hashMap = new HashMap<>();
            //이미 가입했으면
            if (userInfo != null) {
                hashMap.put("existingUser", "true");
                hashMap.put("userEmail", userInfo.getEmail());
                hashMap.put("userNickname", userInfo.getNickname());
                hashMap.put("userJwtToken", userInfo.getToken());

                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
            //가입 안했으면
            else {
                hashMap.put("existingUser", "false");
                hashMap.put("userEmail", kakaoUserInfo.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("registUser")
    public ResponseEntity<Object> registUser(String email, String nickname) {
        try {
            //jwt 발급

            //사용자 정보 DB에 저장

            //사용자 정보 보내주기
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
//    @GetMapping("jpaTest")
//    public ResponseEntity<Object> jpaTest(String email, String nickname, String token) {
//        try {
//            userRepository.save(User.builder()
//                    .email(email)
//                    .nickname(nickname)
//                    .token(token)
//                    .build());
//            return ResponseEntity.status(HttpStatus.OK).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
}
