package com.bouquet.api.user.web;


import com.bouquet.api.config.NoAuth;
import com.bouquet.api.user.dto.*;
import com.bouquet.api.user.exception.NotValidateAccessToken;
import com.bouquet.api.user.exception.NotValidateRefreshToken;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.user.service.GoogleAuthService;
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

    private final JWTUtil jwtUtil;
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";


    private final KakaoAuthService kakaoAuthService;


    private final GoogleAuthService googleAuthService;

    private final UserService userService;

    //프론트에서 인증 코드를 보내주면 카카오 서버와 통신하고 사용자 정보를 조회한다.
    @NoAuth
    @ApiOperation(value = "카카오 로그인", notes = "code 값을 입력하여 로그인 후 기존 회원은 existingUser 값 true, socialLoginType 값 1, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false, socialLoginType 값 1, socialId, refreshToken 반환")
    @GetMapping("kakaoLogin")
    public ResponseEntity<Object> kakaoLogin(String code, HttpServletResponse res) {
        System.out.println(code);
        HttpStatus status = null;
        try {
            KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
            KakaoUserInfo kakaoUserInfo = kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());
            User userInfo = kakaoAuthService.socialIdCheck(Long.toString(kakaoUserInfo.getId()));


            HashMap<String, Object> hashMap = new HashMap<>();
            //이미 가입했으면
            if (userInfo != null) {
                UserResponse.UserInfo response = UserResponse.UserInfo.build(userInfo);
                hashMap.put("user", response);
                hashMap.put("socialLoginType", 1);
                hashMap.put("existingUser", "true");
                try {
                    hashMap.put("accessToken", jwtUtil.createToken(Long.toString(response.getId())));
                    hashMap.put("message", SUCCESS);
                    status = HttpStatus.ACCEPTED;
                } catch (Exception e) {
                    hashMap.put("message", FAIL);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                // jwt refresh 토큰 생성 후 쿠키로 전달
                String refreshToken = userService.refreshToken(response.getId());
                res.addHeader("Set-Cookie", "refreshToken="+refreshToken+"; path=/; MaxAge=7 * 24 * 60 * 60; SameSite=Lax; HttpOnly");
                return ResponseEntity.status(status).body(hashMap);
            }
            //가입 안했으면
            else {
                hashMap.put("existingUser", "false");
                hashMap.put("socialLoginType", 1);
                hashMap.put("socialId", kakaoUserInfo.getId());
                hashMap.put("refreshToken", kakaoTokenInfo.getRefreshToken());
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @NoAuth
    @ApiOperation(value = "구글 로그인", notes = "code 값을 입력하여 로그인 후 기존 회원은 existingUser 값 true, socialLoginType 값 0, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false, socialLoginType 값 0, socialId, refreshToken 반환")
    @GetMapping("googleLogin")
    public ResponseEntity<Object> googleLogin(String code, HttpServletResponse res){

        HttpStatus status = null;
        try {
            GoogleTokenInfo googleTokenInfo = googleAuthService.sendCode(code);
            GoogleUserInfo googleUserInfo = googleAuthService.sendToken(googleTokenInfo.getAccessToken());
            User userInfo = googleAuthService.socialIdCheck(Long.toString(googleUserInfo.getId()));

            HashMap<String, Object> hashMap = new HashMap<>();
            //이미 가입했을 때
            if (userInfo != null) {
                UserResponse.UserInfo response = UserResponse.UserInfo.build(userInfo);
                hashMap.put("user", response);
                hashMap.put("socialLoginType", 0);
                hashMap.put("existingUser", "true");
                try {
                    hashMap.put("accessToken", jwtUtil.createToken(Long.toString(response.getId())));
                    hashMap.put("message", SUCCESS);
                    status = HttpStatus.ACCEPTED;
                } catch (Exception e) {
                    hashMap.put("message", FAIL);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                // jwt refresh 토큰 생성 후 쿠키로 전달
                String refreshToken = userService.refreshToken(response.getId());
                res.addHeader("Set-Cookie", "refreshToken="+refreshToken+"; path=/; MaxAge=7 * 24 * 60 * 60; SameSite=Lax; HttpOnly");
                return ResponseEntity.status(status).body(hashMap);
            }
            //가입 안했을 때 프론트로 정보 보내줌
            else {
                hashMap.put("existingUser", "false");
                hashMap.put("socialLoginType", 0);
                hashMap.put("socialId", googleUserInfo.getId());
                hashMap.put("refreshToken", googleTokenInfo.getRefreshToken());
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @NoAuth
    @ApiOperation(value = "닉네임 입력", notes = "nickname, socialId, socialLoginType, refreshToken 값을 입력받아 유저 생성 후 jwt와 user 정보 반환")
    @PostMapping(value = "/login/user/nickname")
    public ResponseEntity<Map<String, Object>> getUser(@RequestBody UserRequest.GetUser request, HttpServletResponse res) {

        HttpStatus status = null;
        HashMap<String, Object> result = userService.create(request);
        if (result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        // jwt refresh 토큰 생성 후 쿠키로 전달
        UserResponse.UserInfo response = (UserResponse.UserInfo) result.get("user");
        String refreshToken = userService.refreshToken(response.getId());
        res.addHeader("Set-Cookie", "refreshToken="+refreshToken+"; path=/; MaxAge=7 * 24 * 60 * 60; SameSite=Lax; HttpOnly");
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
            // 구글 유저 탈퇴
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



    @PostMapping("/retoken")
    public ResponseEntity<TokenResponse.NewToken> reIssue(@RequestBody TokenRequest.Create request, HttpServletResponse res, @CookieValue(name="refreshToken") String refresh) throws NotValidateAccessToken, NotValidateRefreshToken {
        TokenResponse.NewToken response = userService.getNewToken(request, refresh);
        String refreshToken = userService.refreshToken(response.getUserId());
        res.addHeader("Set-Cookie", "refreshToken="+refreshToken+"; path=/; MaxAge=7 * 24 * 60 * 60; SameSite=Lax; HttpOnly");
        return ResponseEntity.ok().body(response);
    }
}

