package com.bouquet.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column
    private String title;

    @Column
    private boolean visibility;

    @Column(nullable = false)
    private String date;

    @Column(name = "created_at")
    private String createdAt;

    public static Post create(PostRequest.Create request) {
        return Post.builder()
                .categoryId(request.getCategoryId())
                .userId(request.getUserId())
                .title(request.getTitle())
                .visibility(request.isVisibility())
                .date(request.getDate())
                .build();
    }

}
