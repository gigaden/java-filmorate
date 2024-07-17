package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);
    private static final int maxLengthOfDescription = 200;

    @Override
    public Collection<Film> getAll() {
        log.info("Коллекция фильмов успешно получена");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        checkFields(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} с id={} успешно добавлен.", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Id не указан");
            throw new ValidationNullException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            checkFields(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Фильм {} с id={} успешно обновлён", oldFilm.getName(), oldFilm.getId());
            return oldFilm;
        }
        log.warn("Фильм с id={} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Film delete() {
        return null;
    }

    // Метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
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
