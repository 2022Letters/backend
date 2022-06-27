package com.bouquet.api.message.exception;

import javax.persistence.EntityNotFoundException;

public class MessageNotFoundException extends EntityNotFoundException {
    public  MessageNotFoundException() {
        super("존재하지 않는 메시지입니다.");
    }
}
