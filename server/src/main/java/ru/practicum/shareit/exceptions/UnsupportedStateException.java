package ru.practicum.shareit.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
        "ru.practicum.shareit.booking"
})
public class UnsupportedStateException extends Exception {
    public UnsupportedStateException() {
    }

    public UnsupportedStateException(String message) {
        super(message);
    }
}
