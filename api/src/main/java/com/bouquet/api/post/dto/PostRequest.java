package com.bouquet.api.post.dto;

import lombok.*;

 public class PostRequest {
     @Getter
     @Builder
     @NoArgsConstructor(access = AccessLevel.PRIVATE)
     @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private Long categoryId;
        private Long userId;
        private String title;
        private boolean visibility;
        private String date;
    }

}
