package com.bouquet.api.post.dto;

import com.bouquet.api.user.dto.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column
    private boolean visibility;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate date;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public static Post create(PostRequest.Create request, User user) {
        return Post.builder()
                .categoryId(request.getCategoryId())
                .user(user)
                .title(request.getTitle())
                .visibility(request.isVisibility())
                .date(request.getDate())
                .build();
    }

}
