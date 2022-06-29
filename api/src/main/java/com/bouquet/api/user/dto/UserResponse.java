package com.bouquet.api.user.dto;

import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.dto.PostResponse;
import lombok.*;

public class UserResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private long id;
        public static UserResponse.OnlyId build(User user) {
            return UserResponse.OnlyId.builder()
                    .id(user.getId())
                    .build();
        }
    }



    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfo {
        private Long id;
        private String nickname;
        private String email;
        private String token;
        public static UserInfo build(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .token(user.getToken()).build();
        }
    }
}
