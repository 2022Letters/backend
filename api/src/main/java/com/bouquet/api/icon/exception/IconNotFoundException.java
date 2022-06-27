package com.bouquet.api.icon.exception;

import javax.persistence.EntityNotFoundException;

public class IconNotFoundException extends EntityNotFoundException {
    public IconNotFoundException() {
        super("존재하지 않는 아이콘입니다.");
    }
}
