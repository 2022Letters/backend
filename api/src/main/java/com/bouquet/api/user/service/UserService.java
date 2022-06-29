package com.bouquet.api.user.service;


import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.dto.MessageResponse;
import com.bouquet.api.message.exception.MessageNotFoundException;
import com.bouquet.api.user.dto.User;
import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.exception.UserNotFoundException;
import com.bouquet.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public UserResponse.UserInfo create(String nickname){
        User user = (User) httpSession.getAttribute("user");
        user.setNickname(nickname);
        User savedUser = userRepository.save(user);
        return UserResponse.UserInfo.build(savedUser);
    }

    public HashMap<String, Object> checknick(){
        HashMap<String, Object> result = new HashMap<>();
        User user = (User) httpSession.getAttribute("user");
        if(user.getNickname() == null){
            result.put("existingUser", "false");
        }else{
            result.put("existingUser", "true");
            result.put("user", user);
            // 토큰도 같이 보내야 함
        }
        return result;
    }

    public UserResponse.OnlyId delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
        return UserResponse.OnlyId.build(user);
    }

}
