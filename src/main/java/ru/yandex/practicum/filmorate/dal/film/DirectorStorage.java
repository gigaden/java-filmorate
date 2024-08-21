package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

@Repository("directorStorage")
public class DirectorStorage extends BaseDbStorage<Director> {
    private static final String FIND_ALL_QUERY_BY_FILM_ID = "SELECT d.id AS id, d.name AS name FROM directors d\n" +
            "JOIN film_director fd ON d.id = fd.director_id\n" +
            "WHERE film_id = ? ORDER BY d.id";
    private static final String GET_ALL_QUERY = "SELECT * FROM directors ORDER BY id";
    private static final String INSERT_FILM_DIRECTOR = "INSERT INTO film_director (film_id, director_id) VALUES(?,?)";
    private static final String GET_DIRECTOR_BY_ID = "SELECT * FROM directors WHERE id = ?";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR_BY_ID = "DELETE from directors WHERE id = ?";

    public DirectorStorage(JdbcTemplate jdbc, DirectorRowMapper mapper) {
        super(jdbc, mapper);
    }

    // Получаем режиссеров по id фильма
    public Collection<Director> findAllByFilmId(long id) {
        return findMany(FIND_ALL_QUERY_BY_FILM_ID, id);
    }

    // Получаем всех возможных режиссеров в БД
    public Collection<Director> getAll() {
        return findMany(GET_ALL_QUERY);
    }

    // Получаем режиссера по id
    public Optional<Director> getDirectorById(int id) {
        return findOne(GET_DIRECTOR_BY_ID, id);
    }
}