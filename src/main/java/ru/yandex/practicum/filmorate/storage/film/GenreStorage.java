package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository("genreStorage")
public class GenreStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY_BY_FILM_ID = "SELECT g.id AS id, g.name AS name FROM genres g\n" +
            "JOIN film_genre fg ON g.id = fg.genre_id\n" +
            "WHERE film_id = ? ORDER BY g.id";
    private static final String GET_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES(?,?)";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public GenreStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> findAllByFilmId(long id) {
        return findMany(FIND_ALL_QUERY_BY_FILM_ID, id);
    }

    public Collection<Genre> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    public Optional<Genre> getGenreById(int id) {
        return findOne(GET_GENRE_BY_ID, id);
    }

    public void insertIntoFilmGenre(long film_id, int genre_id) {
        add(INSERT_FILM_GENRE, film_id, genre_id);
    }




}
