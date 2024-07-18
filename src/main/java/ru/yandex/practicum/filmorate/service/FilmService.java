package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);
    private static final int maxLengthOfDescription = 200;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getAll() {
        log.info("Получаем коллекцию всех Фильмов.");
        return filmStorage.getAll();
    }

    public Film get(Long id) {
        log.info("Попытка получить фильм с id={}", id);
        return filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%d не найден", id)));
    }

    public Film create(Film film) {
        log.info("Попытка добавить новый фильм.");
        if (film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        checkFields(film);
        film.setLikes(new HashSet<>());
        return filmStorage.create(film);

    }

    public Film update(Film newFilm) {
        log.info("Попытка обновить фильм.");
        if (newFilm.getId() == null) {
            log.warn("Id не указан");
            throw new ValidationNullException("Id должен быть указан");
        }
        get(newFilm.getId());
        checkFields(newFilm);
        return filmStorage.update(newFilm);
    }

    public void delete(Long id) {
        log.info("Попытка удалить фильм с id={}.", id);
        get(id);
        filmStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        log.info("Попытка поставить лайк фильму с id={} юзером с id={}.", id, userId);
        Film film = get(id);
        userService.get(userId);
        film.getLikes().add(userId);
        log.info("Фильму с id={} поставил лайк юзер с id={}.", id, userId);


    }

    public void deleteLike(Long id, Long userId) {
        log.info("Попытка убрать лайк фильму с id={} юзером с id={}.", id, userId);
        Film film = get(id);
        userService.get(userId);
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрос на получение популярных фильмов.");
        if (count > filmStorage.getAll().size()) {
            count = filmStorage.getAll().size();
        }
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film el) -> el.getLikes().size()).reversed()).limit(count)
                .collect(Collectors.toList());
    }

    // Валидируем поля
    private void checkFields(Film film) {
        if (film.getDescription().length() > maxLengthOfDescription) {
            log.warn("Длина описания фильма = {} превышает максимально допустимое = {}",
                    film.getDescription().length(), maxLengthOfDescription);
            throw new ValidationException("Максимальная длина описания фильма " + maxLengthOfDescription);
        }
        if (film.getReleaseDate().isBefore(releaseDate)) {
            log.warn("Дата релиза фильма {} раньше, чем {}", film.getReleaseDate(), releaseDate);
            throw new ValidationException("Дата релиза фильма должна быть не раньше " + releaseDate);
        }
        if (film.getDuration() <= 0) {
            log.warn("Указана неверная продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
