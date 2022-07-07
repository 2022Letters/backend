package com.bouquet.api.message.dto;

import com.bouquet.api.post.dto.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "icon_id")
    private int iconId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String content;

    @Column
    private Double x;

    @Column
    private Double y;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public static Message create(MessageRequest.Create request, Post post) {
        return Message.builder()
                .post(post)
                .iconId(request.getIconId())
                .nickname(request.getNickname())
                .content(request.getContent())
                .x(request.getX())
                .y(request.getY())
                .build();
    }
}
