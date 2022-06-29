package com.bouquet.api.user.web;


import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.dto.KakaoUserInfo;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.user.service.KakaoAuthService;
import com.bouquet.api.user.service.UserService;
import com.bouquet.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping("kakaoLogin")
    public ResponseEntity<Object> kakaoLogin(String code, HttpSession httpSession) {
        System.out.print(code);
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
                hashMap.put("existingUser", "true");
                try{
                    hashMap.put("access-token", jwtUtil.createToken("email", userInfo.getEmail()));
                    hashMap.put("message", SUCCESS );
                    status = HttpStatus.ACCEPTED;
                }catch(Exception e){
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
                hashMap.put("existingUser", "false");
                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


//    @GetMapping(value = "/login/user/nickname")
//    public ResponseEntity<UserResponse.UserInfo> getUser(String nickname){
//        UserResponse.UserInfo response = userService.create(nickname);
//        return new ResponseEntity<UserResponse.UserInfo>(response, HttpStatus.OK);
//    }


    @GetMapping(value = "/login/user/nickname")
    public ResponseEntity<Map<String, Object>> getUser(String nickname){
        HttpStatus status = null;
        HashMap<String, Object> result = userService.create(nickname);
        if(result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else status = status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @GetMapping(value = "/login/sucess")
    public ResponseEntity<Map<String, Object>> loginComplete() {
        HttpStatus status = null;
        HashMap<String, Object> result = userService.checknick();
        if(result.get("existingUser").equals("false"))
            status = HttpStatus.ACCEPTED;
        else if(result.get("existingUser").equals("true") && result.get("message").equals("success"))
            status = HttpStatus.ACCEPTED;
        else status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<Map<String, Object>>(result, status);
    }

    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<UserResponse.OnlyId> delete(@PathVariable Long userId) {
        UserResponse.OnlyId response = userService.delete(userId);
        return ResponseEntity.ok().body(response);
    }

}
