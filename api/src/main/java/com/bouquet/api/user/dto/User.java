package com.bouquet.api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length = 255, nullable = true)
    private String token;

    @Builder
    public User(String email, String nickname, String token) {
        this.email = email;
        this.nickname = nickname;
        this.token = token;
    }
}
