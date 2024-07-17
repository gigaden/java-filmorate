package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // Обрабатываем запрос на получение всех фильмов
    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    // Получаем фильм по id
    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        return filmService.get(id);
    }

    // Обрабатываем запрос на добавление фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    // Обрабатываем запрос на изменение фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    // Удаляем фильм по id
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmService.delete(id);
    }

    // Пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    // Пользователь убирает лайк фильму
    @DeleteMapping("/{id}/like/{userId}")
    public void delLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.delLike(id, userId);
    }

    // Получаем популярные фильмы
    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }


}
