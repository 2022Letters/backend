package com.bouquet.api.post.dto;

import lombok.*;

public class PostResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private long id;
        public static PostResponse.OnlyId build(Post post) {
            return PostResponse.OnlyId.builder()
                    .id(post.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPost {
        private Long id;
        private Long categoryId;
        private Long userId;
        private String title;
        private boolean visibility;
        private String date;
        private String createdAt;
        public static PostResponse.GetPost build(Post post) {
            return GetPost.builder()
                    .id(post.getId())
                    .categoryId(post.getCategoryId())
                    .userId(post.getUserId())
                    .title(post.getTitle())
                    .visibility(post.isVisibility())
                    .date(post.getDate())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }

}
