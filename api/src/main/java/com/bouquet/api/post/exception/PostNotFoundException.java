package com.bouquet.api.post.exception;

import javax.persistence.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {
    public  PostNotFoundException() {
        super("존재하지 않는 게시글입니다.");
    }
}