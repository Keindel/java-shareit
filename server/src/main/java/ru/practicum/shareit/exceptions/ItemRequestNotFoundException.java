package ru.practicum.shareit.exceptions;

public class ItemRequestNotFoundException extends Exception {
    public ItemRequestNotFoundException() {
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
