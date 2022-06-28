package com.bouquet.api.message.dto;


import com.bouquet.api.icon.dto.Icon;
import com.bouquet.api.post.dto.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String content;

    @Column
    private int x;

    @Column
    private int y;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Message create(MessageRequest.Create request, Post post, Icon icon) {
        return Message.builder()
                .post(post)
                .icon(icon)
                .nickname(request.getNickname())
                .content(request.getContent())
                .x(request.getX())
                .y(request.getY())
                .build();
    }
}
