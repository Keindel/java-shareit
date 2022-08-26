package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = {"ru.practicum.shareit.item",
        "ru.practicum.shareit.user",
        "ru.practicum.shareit.requests",
        "ru.practicum.shareit.comment",
        "ru.practicum.shareit.booking",
})
public class ErrorHandler {
    @ExceptionHandler({ItemNotFoundException.class,
            UserNotFoundException.class,
            RequestNotFoundException.class,
            ReviewNotFoundException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final Exception e) {
        return Map.of("error: ", "object not found");
    }

    @ExceptionHandler({BookingValidationException.class,
            CommentValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(final Exception e) {
        return Map.of("error: ", "check your request");
    }

    @ExceptionHandler({UnsupportedStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupported(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}