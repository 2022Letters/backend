package com.bouquet.api.post.dto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
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
    private int categoryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column
    private String title;

    @Column
    private boolean visibility;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

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
