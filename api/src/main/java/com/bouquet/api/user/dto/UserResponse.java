package com.bouquet.api.user.dto;

import lombok.*;

public class UserResponse {

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