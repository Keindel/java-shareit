package ru.practicum.shareit.exceptions;

public class CommentValidationException extends Exception{
    public CommentValidationException() {
    }

    public CommentValidationException(String message) {
        super(message);
    }
}
