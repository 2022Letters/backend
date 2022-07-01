package com.bouquet.api.message.dto;

import lombok.*;

public class MessageRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private Long postId;
        private int iconId;
        private String nickname;
        private String content;
        private Double x;
        private Double y;
    }
}
