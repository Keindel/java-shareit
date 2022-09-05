package ru.practicum.shareit.exceptions;

public class BookingValidationException extends Exception {
    public BookingValidationException() {
    }

    public BookingValidationException(String message) {
        super(message);
    }
}
