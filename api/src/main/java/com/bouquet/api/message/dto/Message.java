package com.bouquet.api.message.dto;


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
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;

    @Column(name = "post_id")
    private Long postId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String content;

    @Column
    private int x;

    @Column
    private int y;

    public static Message create(MessageRequest.Create request) {
        return Message.builder()
                .postId(request.getPostId())
                .nickname(request.getNickname())
                .content(request.getContent())
                .x(request.getX())
                .y(request.getY())
                .build();
    }
}
