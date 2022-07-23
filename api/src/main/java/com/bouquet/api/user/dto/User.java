package com.bouquet.api.user.dto;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int socialLoginType;

    @Column(length = 255, nullable = false)
    private String socialId;

    @Column(length = 255)
    private String refreshToken;

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public static User create(User user) {
        return User.builder()
                .nickname(user.getNickname())
                .socialLoginType(user.getSocialLoginType())
                .socialId(user.getSocialId())
                .refreshToken(user.getRefreshToken())
                .build();
    }

}
