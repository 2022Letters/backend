package com.bouquet.api.post.dto;

import lombok.*;

import java.time.LocalDate;

public class PostRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private int categoryId;
        private Long userId;
        private String title;
        private boolean visibility;
        private LocalDate date;
    }
}
