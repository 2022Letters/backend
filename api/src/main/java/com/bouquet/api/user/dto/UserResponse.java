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
        public static UserInfo build(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .build();
        }
    }

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
}
