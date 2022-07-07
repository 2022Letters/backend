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
    private String email;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length = 255)
    private String refreshToken;

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public static User create(User user) {
        return User.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .refreshToken(user.getRefreshToken())
                .build();
    }

}
