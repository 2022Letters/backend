package com.bouquet.api.user.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("존재하지 않는 사용자입니다.");
    }
}