package ru.yandex.practicum.filmorate.dal.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Slf4j
@Repository("ratingStorage")
public class RatingStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_RATING_BY_FILM_ID = """
            SELECT users_id
            FROM ratings
            WHERE films_id = ?
            """;
    private static final String ADD_RATING_TO_FILMS_QUERY = """
            INSERT INTO ratings(films_id, users_id, rating)
            VALUES (?,?,?)
            """;
    private static final String UPDATE_RATING_FILMS_QUERY = """
            UPDATE films
            SET rating = (
                SELECT AVG(rating)
                FROM ratings
                WHERE films_id = ?)
            WHERE id = ?
            """;
    private static final String DELETE_RATING_QUERY = """
            DELETE from ratings
            WHERE films_id = ? AND users_id = ?
            """;

    public RatingStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    /**
     * Получаем ID юзеров которые поставили рейтинг фильму.
     */
    public Collection<Long> findUsersByFilmRating(long filmId) {
        log.info("Получение всех ID юзеров поставивших рейтинг фильму с ID - {}", filmId);
        return jdbc.queryForList(FIND_ALL_RATING_BY_FILM_ID, Long.class, filmId);
    }

    /**
     * Добавляем фильму рейтинг через сводную таблицу.
     */
    public void addRatingByFilm(Long filmId, Long userId, Integer rating) {
        log.info("Добавление записи рейтинга фильу ID - {} , юзер ID - {} , рейтинг - {}", filmId,userId,rating);
        add(ADD_RATING_TO_FILMS_QUERY, filmId, userId, rating);
        add(UPDATE_RATING_FILMS_QUERY, filmId, filmId);
        log.info("Добавлена запись рейтинга фильу ID - {} , юзер ID - {} , рейтинг - {}", filmId,userId,rating);
    }

    /**
     * Удаляем рейтинг у фильма.
     */
    public void deleteRatingByFilm(Long filmId, Long userId) {
        log.info("Удаление записи рейтинга фильу ID - {} , юзер ID - {}", filmId,userId);
        delete(DELETE_RATING_QUERY, filmId, userId);
        add(UPDATE_RATING_FILMS_QUERY, filmId, filmId);
        log.info("Удалена запись рейтинга фильу ID - {} , юзер ID - {}", filmId,userId);
    }
}