package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Deprecated
@Data
public class Like {
    @NonNull
    private long film_id;
    @NonNull
    private long user_id;

    public Like() {
    }
}
