package com.bouquet.api.message.dto;

import lombok.*;

import java.sql.Timestamp;

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
        private Long msgId;
        private Long postId;
        private int iconId;
        private String nickname;
        private String content;
        private Double x;
        private Double y;
        private Timestamp createdAt;

        public static MessageResponse.GetMessage build(Message message) {
            return GetMessage.builder()
                    .msgId(message.getId())
                    .postId(message.getPost().getId())
                    .iconId(message.getIconId())
                    .nickname(message.getNickname())
                    .content(message.getContent())
                    .x(message.getX())
                    .y(message.getY())
                    .createdAt(message.getCreatedAt())
                    .build();
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetMessageInfo {
        private Long msgId;
        private String nickname;
        private int iconId;
        private Double x;
        private Double y;
        public static MessageResponse.GetMessageInfo build(Message message) {
            return GetMessageInfo.builder()
                    .msgId(message.getId())
                    .nickname(message.getNickname())
                    .iconId(message.getIconId())
                    .x(message.getX())
                    .y(message.getY())
                    .build();
        }
    }
}
