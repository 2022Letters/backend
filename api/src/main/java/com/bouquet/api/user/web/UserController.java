package com.bouquet.api.user.web;


import com.bouquet.api.config.NoAuth;
import com.bouquet.api.user.dto.*;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.user.service.KakaoAuthService;
import com.bouquet.api.user.service.UserService;
import com.bouquet.api.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private JWTUtil jwtUtil;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private KakaoAuthService kakaoAuthService;

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    //프론트에서 인증 코드를 보내주면 카카오 서버와 통신하고 사용자 정보를 조회한다.
    @NoAuth
    @ApiOperation(value = "카카오 로그인", notes = "code 값을 입력하여 로그인 후 기존 회원은 existingUser 값 true, socialLoginType 값 1, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false, socialLoginType 값 0, email 반환")
    @GetMapping("kakaoLogin")
    public ResponseEntity<Object> kakaoLogin(String code, @ApiIgnore HttpSession httpSession) {
        System.out.println(code);
        HttpStatus status = null;
        try {
            KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
            KakaoUserInfo kakaoUserInfo = kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());
            User userInfo = kakaoAuthService.emailCheck(kakaoUserInfo.getEmail());


            HashMap<String, Object> hashMap = new HashMap<>();
            //이미 가입했으면
            if (userInfo != null) {
                UserResponse.UserInfo response = UserResponse.UserInfo.build(userInfo);
                hashMap.put("user", response);
                hashMap.put("socialLoginType", 1);
                hashMap.put("existingUser", "true");
                try {
                    hashMap.put("accessToken", jwtUtil.createToken("email", userInfo.getEmail()));
                    hashMap.put("message", SUCCESS);
                    status = HttpStatus.ACCEPTED;
                } catch (Exception e) {
                    hashMap.put("message", FAIL);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                return ResponseEntity.status(status).body(hashMap);
            }
            //가입 안했으면
            else {
                User user = new User();
                user.setEmail(kakaoUserInfo.getEmail());
                user.setRefreshToken(kakaoTokenInfo.getRefreshToken());
                httpSession.setAttribute("user", user);
                hashMap.put("existingUser", "false");
                hashMap.put("socialLoginType", 1);
                hashMap.put("email", user.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @NoAuth
    @ApiOperation(value = "닉네임 입력", notes = "nickname, email 값을 입력받아 유저 생성 후 jwt와 user 정보 반환")
    @PostMapping(value = "/login/user/nickname")
    public ResponseEntity<Map<String, Object>> getUser(@RequestBody User user) {
        System.out.println(user.toString());
        HttpStatus status = null;
        HashMap<String, Object> result = userService.create(user);
        if (result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else status = status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<Map<String, Object>>(result, status);
    }
    @NoAuth
    @ApiOperation(value = "구글 로그인 후 유저 정보 반환", notes = "email 값을 입력받아 기존 회원은 existingUser 값 true, socialLoginType 값 0, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false, socialLoginType 값 0으로 반환 ")
    @GetMapping(value = "/login/sucess")
    public ResponseEntity<Map<String, Object>> loginComplete(HttpSession httpSession, String email) {
//
        HttpStatus status = null;
        User user = new User();
        if (userRepository.existsByEmail(email) != null) {
            user = userRepository.existsByEmail(email);
        } else {
            user.setEmail(email);
        }
        System.out.println(email + "이메일");
        System.out.println(user + "찾은유저정보");
        httpSession.setAttribute("user", user);
        HashMap<String, Object> result = userService.checknick(user);
        if (result.get("existingUser").equals("false")) {
            result.put("email", email);
            status = HttpStatus.ACCEPTED;
        } else if (result.get("existingUser").equals("true") && result.get("message").equals("success")) {
            status = HttpStatus.ACCEPTED;
        } else status = HttpStatus.INTERNAL_SERVER_ERROR;
        result.put("socialLoginType", 0);
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "userId 값을 입력받아 회원 탈퇴")
    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId, int socialLoginType) {
        System.out.println("userId : " + userId + ", socialLoginType : " + socialLoginType);

        HashMap<String, Object> hashMap = new HashMap<>();
        //카카오 유저 탈퇴
        if (socialLoginType == 1) {
            //카카오 로그아웃
            try {
                String accessToken = kakaoAuthService.refreshToken(userId);
                kakaoAuthService.kakaoUnlink(accessToken);
                userService.delete(userId);
                hashMap.put("deleteUser", true);
                return ResponseEntity.ok().body(hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                userService.delete(userId);
                hashMap.put("deleteUser", true);
                return ResponseEntity.ok().body(hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hashMap.put("deleteUser", false);
        return ResponseEntity.internalServerError().body(hashMap);
    }
    @NoAuth
    @ApiOperation(value = "리다이랙트 url", notes = "구글 로그인 시 url 뒤에 파라미터로 이메일을 넘김")
    @GetMapping(value = "/login/email")
    public void oauthLogin(HttpServletResponse response, HttpSession httpSession) throws IOException {
        User user = (User) httpSession.getAttribute("user");
        String redirect_uri = "http://localhost:3000/login/redirect";
        response.sendRedirect(redirect_uri + "?email=" + user.getEmail());
    }


}

