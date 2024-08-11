package ru.yandex.practicum.filmorate.exception;

public class HasNoFriendException extends RuntimeException {
    public HasNoFriendException(String message) {
        super(message);
    }
}