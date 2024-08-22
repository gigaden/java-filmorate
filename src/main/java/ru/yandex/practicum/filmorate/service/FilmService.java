package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final LikeService likeService;
    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);
    private static final int maxLengthOfDescription = 200;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserService userService, MpaService mpaService, GenreService genreService,
                       LikeService likeService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaService = mpaService;
        this.genreService = genreService;
        this.likeService = likeService;
    }

    public Collection<Film> getAll() {
        log.info("Получаем коллекцию всех Фильмов.");
        Collection<Film> films = filmStorage.getAll();
        // Пишем подгружаемые из других таблиц поля в фильм
        for (Film film : films) {
            setFilmFields(film);
        }
        log.info("Фильмы успешно переданы.  ");
        return films;
    }

    public Film get(Long id) {
        log.info("Попытка получить фильм с id={}.", id);
        Film film = filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%d не найден.", id)));
        setFilmFields(film);
        log.info("Фильм с id = {} успешно передан.", id);
        return film;
    }

    public Film create(Film film) {
        log.info("Попытка добавить новый фильм.");
        if (film.getName().isBlank()) {
            log.warn("Название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым.");
        }
        checkFields(film);
        film.setLikes(new HashSet<>());
        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addGenreToFilm(film.getId(), film.getGenres());
        }
        setFilmFields(film);
        log.info("Добавлен новый фильм с id = {}.", film.getId());

        return film;

    }

    public Film update(Film newFilm) {
        log.info("Попытка обновить фильм.");
        if (newFilm.getId() == null) {
            log.warn("Id не указан.");
            throw new ValidationNullException("Id должен быть указан.");
        }
        get(newFilm.getId());
        checkFields(newFilm);
        Film film = filmStorage.update(newFilm);
        log.info("Обновлён фильм с id = {}.", film.getId());
        return film;
    }

    public void delete(Long id) {
        log.info("Попытка удалить фильм с id={}.", id);
        get(id);
        filmStorage.delete(id);
        log.info("Фильм с id = {} удалён.", id);
    }

    public void addLike(Long id, Long userId) {
        log.info("Попытка поставить лайк фильму с id={} юзером с id={}.", id, userId);
        Film film = get(id);
        userService.get(userId);
        likeService.addLike(id, userId);
        log.info("Фильму с id={} поставил лайк юзер с id={}.", id, userId);


    }

    public void deleteLike(Long id, Long userId) {
        log.info("Попытка убрать лайк фильму с id={} юзером с id={}.", id, userId);
        Film film = get(id);
        userService.get(userId);
        likeService.delLike(id, userId);
        log.info("Пользователь с id = {} убрал лайк фильму с id = {}", userId, id);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Запрос на получение популярных фильмов.");
        if (count > getAll().size()) {
            count = getAll().size();
        }
        List<Film> popularFilms = getAll().stream()
                .sorted(Comparator.comparingInt((Film el) -> el.getLikes().size()).reversed()).limit(count)
                .collect(Collectors.toList());
        log.info("Коллекция популярных фильмов успешно отправлена.");
        return popularFilms;
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
        if (film.getGenres() != null) {
            Collection<Integer> genresId = genreService.getAllGenresID();
            for (Genre genre : film.getGenres()) {
                if (!genresId.contains(genre.getId())) {
                    log.warn("Указан не существующий id жанра: {}", genre.getId());
                    throw new ValidationException("Указан не существующий id жанра.");
                }
            }
        }
        if (film.getMpa() != null) {
            if (!mpaService.getAllMpasId().contains(film.getMpa().getId())) {
                log.warn("Указан не существующий id рейтинга: {}", film.getMpa().getId());
                throw new ValidationException("Указан не существующий id рейтинга.");
            }
        }
    }

    // Возвращаем рекомендации по фильмам для просмотра
    public Collection<Film> getRecommendedFilms(Long id) {
        log.info("Пытаемся получить коллекцию рекомендованных фильмов");
        final Collection<Film> films = filmStorage.getRecommendedFilms(id);
        for (Film film : films) {
            setFilmFields(film);
        }
        log.info("Рекомендованные фильмы успешно переданы");
        return films;
    }

    // Добавляем поля в фильм
    private void setFilmFields(Film film) {
        film.setMpa(mpaService.get(film.getMpa().getId()));
        //film.setGenres(new HashSet<>(genreService.getAllFilmGenreById(film.getId())));
        film.setGenres(new LinkedHashSet<>(genreService.getAllFilmGenreById(film.getId())));
        film.setLikes(new HashSet<>(likeService.getLikesByFilmId(film.getId())));
    }
}
