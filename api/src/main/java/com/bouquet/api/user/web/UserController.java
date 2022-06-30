package com.bouquet.api.user.web;


import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
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

import javax.servlet.http.HttpSession;
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
    @ApiOperation(value = "카카오 로그인", notes = "code 값을 입력하여 로그인 후 기존 회원은 existingUser 값 true, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false로 반환")
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
                httpSession.setAttribute("user", user);
                hashMap.put("socialLoginType", 1);
                hashMap.put("existingUser", "false");
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ApiOperation(value = "닉네임 입력", notes = "nickname 값을 입력받아 유저 생성 후 jwt와 user 정보 반환")
    @GetMapping(value = "/login/user/nickname")
    public ResponseEntity<Map<String, Object>> getUser(String nickname) {
        HttpStatus status = null;
        HashMap<String, Object> result = userService.create(nickname);
        if (result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else status = status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @ApiOperation(value = "구글 로그인 후 유저 정보 반환", notes = "기존 회원은 existingUser 값 true, jwt, user 정보 반환, 미가입 회원일 경우 existingUser 값 false로 반환 ")
    @GetMapping(value = "/login/sucess")
    public ResponseEntity<Map<String, Object>> loginComplete() {
        HttpStatus status = null;
        HashMap<String, Object> result = userService.checknick();
        if (result.get("existingUser").equals("false"))
            status = HttpStatus.ACCEPTED;
        else if (result.get("existingUser").equals("true") && result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "userId 값을 입력받아 회원 탈퇴")
    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<UserResponse.OnlyId> delete(@PathVariable Long userId) {
        UserResponse.OnlyId response = userService.delete(userId);
        return ResponseEntity.ok().body(response);
    }

}
