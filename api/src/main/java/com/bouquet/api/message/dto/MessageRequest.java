package com.bouquet.api.message.dto;

import lombok.*;

public class MessageRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private Long postId;
        private String nickname;
        private String content;
        private int x;
        private int y;
    }
}
