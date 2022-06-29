package com.bouquet.api.post.dto;

import com.bouquet.api.message.dto.Message;
import com.bouquet.api.message.dto.MessageResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        private int categoryId;
        private Long userId;
        private String userNickname;
        private String title;
        private boolean visibility;
        private LocalDateTime date;
        private LocalDateTime createdAt;
        private int count;
        private List<MessageResponse.GetMessageInfo> messages;
        public static PostResponse.GetPost build(Post post, int count, List<Message> messages) {
            return GetPost.builder()
                    .id(post.getId())
                    .categoryId(post.getCategoryId())
                    .userId(post.getUser().getId())
                    .userNickname(post.getUser().getNickname())
                    .title(post.getTitle())
                    .visibility(post.isVisibility())
                    .date(post.getDate())
                    .createdAt(post.getCreatedAt())
                    .count(count)
                    .messages(messages.stream().map(MessageResponse.GetMessageInfo::build).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostInfo {
        private Long id;
        private String title;
        private LocalDateTime date;
        private int categoryId;
        private String userNickname;

        public static PostResponse.GetPostInfo build(Post post) {
            return GetPostInfo.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .date(post.getDate())
                    .categoryId(post.getCategoryId())
                    .userNickname(post.getUser().getNickname())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPosts {
        private List<GetPostInfo> posts;

        public static PostResponse.GetPosts build(List<Post> posts) {
            return PostResponse.GetPosts.builder()
                    .posts(posts.stream().map(PostResponse.GetPostInfo::build).collect(Collectors.toList()))
                    .build();
        }
    }



}
