package com.bouquet.api.user.service;


import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.exception.UserNotFoundException;
import com.bouquet.api.user.repository.UserRepository;
import com.bouquet.api.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private JWTUtil jwtUtil;

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public HashMap<String, Object> create(User user) {
        HashMap<String, Object> result = new HashMap<>();
        User savedUser = userRepository.save(user);
        UserResponse.UserInfo response = UserResponse.UserInfo.build(savedUser);
        result.put("user", response);
        try {
            result.put("accessToken", jwtUtil.createToken("email", user.getEmail()));
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
                result.put("accessToken", jwtUtil.createToken("email", user.getEmail()));
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


}
