package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final DirectorService directorService;

    @Autowired
    public FilmController(FilmService filmService, DirectorService directorService) {
        this.filmService = filmService;
        this.directorService = directorService;
    }

    // Обрабатываем запрос на получение всех фильмов
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    // Получаем фильм по id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable Long id) {
        return filmService.get(id);
    }

    // Обрабатываем запрос на добавление фильма
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    // Обрабатываем запрос на изменение фильма
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    // Удаляем фильм по id
    @DeleteMapping("/{filmId}")
    public void delete(@PathVariable Long filmId) {
        filmService.delete(filmId);
    }

    // Пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    // Пользователь убирает лайк фильму
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    // Получаем популярные фильмы
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilm(@RequestParam(value = "count", defaultValue = "10") int count,
                                           @RequestParam(value = "genreId", required = false) Integer genreId,
                                           @RequestParam(value = "year", required = false) Integer year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    // Получаем фильмы по названию и по режиссёру
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilmsByParams(@RequestParam String query,
                                             @RequestParam Collection<String> by) {
        return filmService.getFilmsByParams(query, by);
    }

    @GetMapping("/common")
    public Collection<Film> getSharedFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getSharedFilms(userId, friendId);
    }

    // Получаем список фильмов режиссера, отсортированный по году или лайкам
    @GetMapping("/director/{directorId}")
    public Collection<Film> getFilmsByDirector(@PathVariable("directorId") Long directorId,
                                               @RequestParam(required = false) List<String> sortBy) {
        return filmService.getSortedDirectorFilms(directorId, sortBy);
    }
}