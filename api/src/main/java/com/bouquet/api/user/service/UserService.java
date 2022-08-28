package com.bouquet.api.user.service;


import com.bouquet.api.user.dto.*;
import com.bouquet.api.user.exception.NotValidateAccessToken;
import com.bouquet.api.user.exception.NotValidateRefreshToken;
import com.bouquet.api.user.exception.UserNotFoundException;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.util.JWTUtil;
import com.bouquet.api.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    // JWT refresh 토큰 생성 (유효기간 7일)
    public String refreshToken(Long userId) {
        String refreshToken = jwtUtil.createRefreshToken();
        redisUtil.setDataExpire(Long.toString(userId), refreshToken, 1000L * 60 * 60 * 24 * 7); // 7일
        return refreshToken;
    }

    // jwt 토큰 재발급
    public TokenResponse.NewToken getNewToken(TokenRequest.Create request, String refreshToken) throws NotValidateRefreshToken, NotValidateAccessToken {
        // accessToken에 담긴 user의 id를 이용하여 user 정보 찾음
        Optional<User> user = this.findUserByToken(request.getAccessToken());

        // 해당 토큰으로 찾은 user의 정보가 없으면 유효하지 않은 accessToken 에러 발생
        if(user.equals(Optional.empty())){
            throw new NotValidateAccessToken();
        }
        // redis에 user의 id 값으로 저장한 refreshToken 값 반환
        String savedRefreshtoken = redisUtil.getData(Long.toString(user.get().getId()));
        // refreshToken의 유효시간 만료 또는 저장된 refreshToken과 요청받은 refreshToken이 일치하지 않을 경우 에러 발생
        if(savedRefreshtoken==null || !savedRefreshtoken.equals(refreshToken)){
            throw new NotValidateRefreshToken();
        }
        // 새로운 accessToken 생성
        String accessToken = jwtUtil.createToken(Long.toString(user.get().getId()));
        return TokenResponse.NewToken.build(accessToken, user.get().getId() );
    }

    // accessToken 생성 시 사용된 user의 id 값을 찾아 해당 id를 가지는 user 반환
    public Optional<User> findUserByToken(String accessToken) throws NotValidateAccessToken {
        String userId = jwtUtil.getUserId(accessToken);
        return userRepository.findById(Long.parseLong(userId));
    }


}
