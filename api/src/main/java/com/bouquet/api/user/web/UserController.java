package com.bouquet.api.user.web;

import com.bouquet.api.user.dto.UserResponse;
import com.bouquet.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserController {

    private final UserService userService;



//    @GetMapping(value = "/user/nickname")
//    public ResponseEntity<User> getUser(String nickname, HttpSession session){
//        User user = (User) session.getAttribute("user");
////        user.update(nickname);
//        System.out.println(user);
//        return new ResponseEntity<User>(user, HttpStatus.OK);
//    }

    @GetMapping(value = "/user/nickname")
    public ResponseEntity<UserResponse.UserInfo> getUser(String nickname){
        UserResponse.UserInfo response = userService.create(nickname);
        return new ResponseEntity<UserResponse.UserInfo>(response, HttpStatus.OK);
    }

//    @GetMapping(value = "/sucess")
//    public ResponseEntity<User> loginComplete(HttpSession session) {
//
//        User user = (User) session.getAttribute("user");
//        return new ResponseEntity<User>(user, HttpStatus.OK);
//    }

    @GetMapping(value = "/sucess")
    public ResponseEntity<Map<String, Object>> loginComplete(HttpSession session) {
        HashMap<String, Object> result = userService.checknick();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

}
