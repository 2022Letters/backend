package com.bouquet.api.user.web;


import com.bouquet.api.user.dto.KakaoTokenInfo;
import com.bouquet.api.user.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {
    @Autowired
    private KakaoAuthService kakaoAuthService;

    @RequestMapping("oauth")
    public ResponseEntity<Object> auth(String code) {
        //System.out.print(code);
        try {
            KakaoTokenInfo kakaoTokenInfo = kakaoAuthService.sendCode(code);
            kakaoAuthService.sendToken(kakaoTokenInfo.getAccessToken());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
