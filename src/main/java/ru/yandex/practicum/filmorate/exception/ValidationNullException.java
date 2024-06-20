package ru.yandex.practicum.filmorate.exception;

public class ValidationNullException extends RuntimeException {
    public ValidationNullException(String message) {
        super(message);
    }
}