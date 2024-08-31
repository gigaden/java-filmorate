package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository("genreStorage")
public class GenreStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY_BY_FILM_ID = """
            SELECT g.id AS id, g.name AS name FROM genres g
            JOIN film_genre fg ON g.id = fg.genre_id
            WHERE film_id = ? ORDER BY g.id
            """;
    private static final String GET_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES(?,?)";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";
    private static final String DELETE_FILM_GENRE = "DELETE FROM film_genre WHERE film_id = ?";

    public GenreStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    // Получаем все жанры фильма по id
    public Collection<Genre> findAllByFilmId(long id) {
        return findMany(FIND_ALL_QUERY_BY_FILM_ID, id);
    }

    // Получаем все возможные жанры в БД
    public Collection<Genre> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    // Получаем жанр по id
    public Optional<Genre> getGenreById(int id) {
        return findOne(GET_GENRE_BY_ID, id);
    }

    // Добавляем фильму жанр, занося данные в сводную таблицу
    public void insertIntoFilmGenre(long filmId, int genreId) {
        add(INSERT_FILM_GENRE, filmId, genreId);
    }

    public void removeGenreByFilmId(Long filmId) {
        delete(DELETE_FILM_GENRE, filmId);
    }
}