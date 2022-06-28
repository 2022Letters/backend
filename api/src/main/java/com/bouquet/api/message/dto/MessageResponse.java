package com.bouquet.api.message.dto;

import lombok.*;
import java.time.LocalDateTime;

public class MessageResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private Long id;
        public static MessageResponse.OnlyId build(Message message) {
            return MessageResponse.OnlyId.builder()
                    .id(message.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetMessage {
        private Long id;
        private Long postId;
        private String iconUrl;
        private String nickname;
        private String content;
        private int x;
        private int y;
        private LocalDateTime createdAt;

        public static MessageResponse.GetMessage build(Message message) {
            return GetMessage.builder()
                    .id(message.getId())
                    .postId(message.getPost().getId())
                    .iconUrl(message.getIcon().getIconUrl())
                    .nickname(message.getNickname())
                    .content(message.getContent())
                    .x(message.getX())
                    .y(message.getY())
                    .createdAt(message.getCreatedAt())
                    .build();
        }
    }
}
