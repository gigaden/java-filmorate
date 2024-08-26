package ru.yandex.practicum.filmorate.dal.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("inMemoryFilmStorage")
@Slf4j
@Deprecated
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        log.info("Коллекция фильмов успешно получена");
        return films.values();
    }

    @Override
    public Optional<Film> get(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} с id={} успешно добавлен.", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        log.info("Фильм {} с id={} успешно обновлён", oldFilm.getName(), oldFilm.getId());
        return oldFilm;
    }

    @Override
    public void delete(Long id) {
        films.remove(id);
        log.info("Фильм с id={} удалён.", id);
    }

    @Override
    public Collection<Film> getAllFilmsByDirectorId(Long directorId) {
        return null;
    }

    // Метод для генерации идентификатора
    @Override
    public Collection<Film> searchByName(String query) {
        return List.of();
    }

    @Override
    public Collection<Film> searchByDirector(String query) {
        return List.of();
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

    @Override
    public Collection<Film> getRecommendedFilms(Long id) {
        return List.of();
    }

    @Override
    public Collection<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return List.of();
    }
}