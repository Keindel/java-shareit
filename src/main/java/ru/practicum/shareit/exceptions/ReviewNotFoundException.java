package ru.practicum.shareit.exceptions;

public class ReviewNotFoundException extends Exception {
    public ReviewNotFoundException() {
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
