package com.bouquet.api.user.web;


import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.user.service.KakaoAuthService;
import com.bouquet.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private UserRepository userRepository;

    //프론트에서 인증 코드를 보내주면 카카오 서버와 통신하고 사용자 정보를 조회한다.
    @RequestMapping("kakaoLogin")
    public ResponseEntity<Object> kakaoLogin(String code, HttpSession httpSession) {
        System.out.print(code);
        try {
            KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
            KakaoUserInfo kakaoUserInfo = kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());
            User userInfo = kakaoAuthService.emailCheck(kakaoUserInfo.getEmail());


            HashMap<String, Object> hashMap = new HashMap<>();
            //이미 가입했으면
            if (userInfo != null) {
                hashMap.put("user", userInfo);
                hashMap.put("existingUser", "true");
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
            //가입 안했으면
            else {
                User user = new User();
                user.setEmail(kakaoUserInfo.getEmail());
                httpSession.setAttribute("user", user);
                hashMap.put("existingUser", "false");
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private final UserService userService;

    @GetMapping(value = "/login/user/nickname")
    public ResponseEntity<UserResponse.UserInfo> getUser(String nickname){
        UserResponse.UserInfo response = userService.create(nickname);
        return new ResponseEntity<UserResponse.UserInfo>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/login/sucess")
    public ResponseEntity<Map<String, Object>> loginComplete(HttpSession session) {
        HashMap<String, Object> result = userService.checknick();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

}
