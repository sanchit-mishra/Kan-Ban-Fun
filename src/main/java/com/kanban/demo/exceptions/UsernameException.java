package com.kanban.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameException extends RuntimeException {
    public UsernameException(String message) {
        super(message);
    }
}
