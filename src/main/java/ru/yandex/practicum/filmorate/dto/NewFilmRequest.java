package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
    @NonNull
    private Mpa mpa;
    private Set<Genre> genre;
    private Set<Long> likes;
}
