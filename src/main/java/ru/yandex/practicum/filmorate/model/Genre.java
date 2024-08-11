package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre() {
    }
}
