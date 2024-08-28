package ru.yandex.practicum.filmorate.dal.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
@Slf4j
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films f JOIN mpas m ON f.mpa = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = """
            INSERT INTO films(name, description, releaseDate, duration, mpa, rating)
            VALUES (?,?,?,?,?,?)
            """;
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?," +
            " releaseDate = ?, duration = ?, mpa = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE from films WHERE id = ?";
    private static final String FIND_FILMS_BY_DIRECTOR_ID = """
            SELECT * FROM films f
            JOIN FILM_DIRECTOR fd ON f.id = fd.FILM_ID
            WHERE fd.DIRECTOR_ID = ?;
            """;
    private static final String FIND_RECOMMENDED_FILMS_QUERY = """
            WITH user_like AS (
                 SELECT films_id, rating
                 FROM ratings
                 WHERE users_id = ?
            ),
            matched_like AS (
                 SELECT l.users_id, COUNT(l.films_id)
                 FROM ratings l
                 INNER JOIN user_like ul ON l.films_id = ul.films_id
                 AND ABS(l.rating - ul.rating) <= 1
                 WHERE l.users_id != ?
                 GROUP BY l.users_id
                 ORDER BY COUNT(l.films_id) DESC
                 LIMIT 1
            )
            SELECT f.*
            FROM ratings l
            LEFT JOIN films f ON f.ID = l.films_id
            INNER JOIN matched_like ml ON ml.users_id = l.users_id
            WHERE l.films_id NOT IN (SELECT films_id FROM ratings WHERE users_id = ?)
            AND f.rating > 5
            """;
// новый запрос неработает с рейтингами
/*    private static final String FIND_RECOMMENDED_FILMS_QUERY22 = """
            WITH user_rating AS (
                 SELECT films_id
                 FROM rating
                 WHERE users_id = ?
            ),
            matched_rating AS (
                 SELECT r.users_id, COUNT(r.films_id)
                 FROM rating r
                 LEFT JOIN user_rating ur ON r.films_id = ur.films_id
                 AND ABS(l1.USER_RATING - l2.USER_RATING) <= 1
                 WHERE r.users_id != ?
                 GROUP BY r.users_id
                 ORDER BY COUNT(r.films_id) DESC
                 LIMIT 1
            )
            SELECT f.*
            FROM rating r
            LEFT JOIN films f ON f.ID = r.films_id
            LEFT JOIN matched_rating mr ON mr.users_id = r.users_id
            WHERE r.films_id NOT IN (SELECT films_id FROM rating WHERE users_id = ?)
            """;*/
    private static final String SEARCH_BY_NAME_AND_QUERY = "SELECT * FROM films WHERE LOWER(name) RATING LOWER('%' || ? || '%')";
    private static final String SEARCH_BY_DIRECTOR_AND_QUERY = """
            SELECT * FROM FILMS WHERE ID in
            (SELECT film_id FROM	FILM_DIRECTOR
            WHERE DIRECTOR_ID in
            (SELECT id FROM DIRECTORS d WHERE LOWER(name) RATING LOWER('%' || ? || '%')));
            """;
    private static final String FIND_POPULAR_FILMS_QUERY = """
            SELECT f.*
            FROM films f
            LEFT JOIN mpas m ON f.mpa = m.id
            LEFT JOIN ratings l ON f.id = l.films_id
            LEFT JOIN film_genre fg ON f.id = fg.film_id
            WHERE (? IS NULL OR YEAR(f.releaseDate) = ?)
            AND (? IS NULL OR fg.GENRE_ID = ?)
            GROUP BY f.id
            ORDER BY COUNT(l.users_id) DESC
            LIMIT ?;
            """;
// новый запрос на популярные фильмы
//    private static final String FIND_POPULAR_FILMS_QUERY = """
//            SELECT f.*
//            FROM films f
//            LEFT JOIN mpas m ON f.mpa = m.id
//            LEFT JOIN likes l ON f.id = l.films_id
//            LEFT JOIN film_genre fg ON f.id = fg.film_id
//            WHERE (? IS NULL OR YEAR(f.releaseDate) = ?)
//            AND (? IS NULL OR fg.GENRE_ID = ?)
//            GROUP BY f.id
//            ORDER BY COUNT(l.users_id) DESC
//            LIMIT ?;
//            """;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    // Получаем все фильмы
    @Override
    public Collection<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<Film> getRecommendedFilms(Long id) {
        return findMany(FIND_RECOMMENDED_FILMS_QUERY, id, id, id);
    }

    @Override
    public Collection<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return findMany(FIND_POPULAR_FILMS_QUERY, year, year, genreId, genreId, count);
    }

    // Получаем фильм по id
    @Override
    public Optional<Film> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    // Добавляем новый фильм
    @Override
    @Transactional
    public Film create(Film film) {
        film.setRating(0.0);
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRating());
        film.setId(id);
        return film;
    }

    // Обновляем фильм
    @Override
    @Transactional
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(), film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    // Удаляем фильм
    @Override
    public void delete(Long id) {
        delete(DELETE_BY_ID, id);
    }

    // Получаем список фильмов по id режиссера
    public Collection<Film> getAllFilmsByDirectorId(Long id) {
        return findMany(FIND_FILMS_BY_DIRECTOR_ID, id);
    }

    // Ищем фильм по совпадению в названии фильма
    @Override
    public Collection<Film> searchByName(String query) {
        return findMany(SEARCH_BY_NAME_AND_QUERY, query);
    }

    // Ищем по совпадению в имени режиссёра
    @Override
    public Collection<Film> searchByDirector(String query) {
        return findMany(SEARCH_BY_DIRECTOR_AND_QUERY, query);
    }
}