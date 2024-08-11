package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
public class Mpa {
    private int id;
    private String name;
    private String description;

    public Mpa() {
    }
}