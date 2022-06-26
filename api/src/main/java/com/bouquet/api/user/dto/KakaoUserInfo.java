package com.bouquet.api.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    long id;
    //String name;
    String email;
    String birthday;

    @JsonProperty("kakao_account")
    private void nested(Map<String, Object> kakaoAccount) {
        //name = ((Map<String, String>) kakaoAccount.get("profile")).get("nickname");
        email = (String) kakaoAccount.get("email");
        birthday = (String) kakaoAccount.get("birthday");
    }
}
