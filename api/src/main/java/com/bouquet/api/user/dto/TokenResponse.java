package com.bouquet.api.user.dto;

import lombok.*;

public class TokenResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NewToken {
        private Long userId;
        private String accessToken;

        public static NewToken build(String accessToken, Long userId) {
            return NewToken.builder()
                    .userId(userId)
                    .accessToken(accessToken)
                    .build();
        }
    }
}
