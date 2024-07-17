package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    // Обрабатываем запрос на получение всех фильмов
    @GetMapping
    public Collection<Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    // Обрабатываем запрос на добавление фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.create(film);

    }

    // Обрабатываем запрос на изменение фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return inMemoryFilmStorage.update(newFilm);
    }


}
