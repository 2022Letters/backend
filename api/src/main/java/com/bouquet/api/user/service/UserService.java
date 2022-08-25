package com.bouquet.api.user.service;


import com.bouquet.api.user.dto.*;
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

    private final JWTUtil jwtUtil;


    private final RedisUtil redisUtil;

    private final UserRepository userRepository;

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public HashMap<String, Object> create(UserRequest.GetUser request) {
        HashMap<String, Object> result = new HashMap<>();
        User user = User.create(request);
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

        if(savedRefreshtoken==null || !savedRefreshtoken.equals(refreshToken)){
            throw new NotValidateRefreshToken();
        }
        String accessToken = jwtUtil.createToken(Long.toString(user.get().getId()));
        return TokenResponse.NewToken.build(accessToken, user.get().getId() );
    }

    public Optional<User> findUserByToken(String accessToken) throws NotValidateAccessToken {
        String userId = jwtUtil.getUserId(accessToken);
        return userRepository.findById(Long.parseLong(userId));
    }


}
