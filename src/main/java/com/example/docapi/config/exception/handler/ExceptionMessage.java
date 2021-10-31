package com.example.docapi.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionMessage {

    private final String message;

    public static ExceptionMessage of(String message) {
        return new ExceptionMessage(message);
    }

}
