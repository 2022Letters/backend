package com.bouquet.api.user.service;


import com.bouquet.api.user.dto.TokenRequest;
import com.bouquet.api.user.dto.TokenResponse;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.exception.NotValidateAccessToken;
import com.bouquet.api.user.exception.NotValidateRefreshToken;
import com.bouquet.api.user.exception.UserNotFoundException;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.util.JWTUtil;
import com.bouquet.api.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    private final UserRepository userRepository;

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public HashMap<String, Object> create(User user) {
        HashMap<String, Object> result = new HashMap<>();
        User savedUser = userRepository.save(user);
        UserResponse.UserInfo response = UserResponse.UserInfo.build(savedUser);
        result.put("user", response);
        try {
            result.put("accessToken", jwtUtil.createToken(Long.toString(response.getId())));
            result.put("message", SUCCESS);
        } catch (Exception e) {
            result.put("message", FAIL);
        }
        return result;
    }

    public HashMap<String, Object> checknick(User user) {
        HashMap<String, Object> result = new HashMap<>();
        if (user.getNickname() == null) {
            result.put("existingUser", "false");
        } else {
            UserResponse.UserInfo response = UserResponse.UserInfo.build(user);
            result.put("existingUser", "true");
            result.put("user", response);
            try {
                result.put("accessToken", jwtUtil.createToken(Long.toString(response.getId())));
                result.put("message", SUCCESS);
            } catch (Exception e) {
                result.put("message", FAIL);
            }
        }
        return result;
    }

    public UserResponse.OnlyId delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
        return UserResponse.OnlyId.build(user);
    }

    // JWT refresh 토큰 생성
    public String refreshToken(Long userId) {
        String refreshToken = jwtUtil.createRefreshToken();
        redisUtil.setDataExpire(Long.toString(userId), refreshToken, 1000L * 60 * 60 * 24 * 7); // 7일
        return refreshToken;
    }

    // 토큰 재발급
    public TokenResponse.NewToken getNewToken(TokenRequest.Create request, String refreshToken) throws NotValidateRefreshToken, NotValidateAccessToken {
        Optional<User> user = this.findUserByToken(request.getAccessToken());
        if(user.equals(Optional.empty())){
            throw new NotValidateAccessToken();
        }
        String savedRefreshtoken = redisUtil.getData(Long.toString(user.get().getId()));
        System.out.println("redis에 저장되어있는 refreshToken : " + savedRefreshtoken);
        System.out.println("받은 refreshTokeb : "+refreshToken);
        if(savedRefreshtoken==null || !savedRefreshtoken.equals(refreshToken)){
            throw new NotValidateRefreshToken();
        }
        String accessToken = jwtUtil.createToken(Long.toString(user.get().getId()));
        return TokenResponse.NewToken.build(accessToken, user.get().getId() );
    }

    public Optional<User> findUserByToken(String accessToken) throws NotValidateAccessToken {
        String userId = jwtUtil.getUserId(accessToken);
        System.out.println(userId+" access토큰으로 찾은 userId");
        return userRepository.findById(Long.parseLong(userId));
    }


}
