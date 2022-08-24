package ru.practicum.shareit.exceptions;

public class UnsupportedStateException extends Exception{
    public UnsupportedStateException() {
    }

    public UnsupportedStateException(String message) {
        super(message);
    }
}
