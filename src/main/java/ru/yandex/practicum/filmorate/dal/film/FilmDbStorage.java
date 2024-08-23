package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.BaseDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films f JOIN mpas m ON f.mpa = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate," +
            "duration, mpa)" +
            "VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?," +
            " releaseDate = ?, duration = ?, mpa = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE from films WHERE id = ?";
    private static final String FIND_RECOMMENDED_FILMS_QUERY = """
            WITH user_like AS (
                 SELECT films_id
                 FROM likes
                 WHERE users_id = ?
            ),
            matched_like AS (
                 SELECT l.users_id, COUNT(l.films_id)
                 FROM likes l
                 JOIN user_like ul ON l.films_id = ul.films_id
                 WHERE l.users_id != ?
                 GROUP BY l.users_id
                 ORDER BY COUNT(l.films_id) DESC
                 LIMIT 1
            )
            SELECT f.*
            FROM likes l
            JOIN films f ON f.ID = l.films_id
            JOIN matched_like ml ON ml.users_id = l.users_id
            WHERE l.films_id NOT IN (SELECT films_id FROM likes WHERE users_id = ?);
            """;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    // Получаем все фильмы
    @Override
    public Collection<Film> getAll() {
        //return jdbc.query(FIND_ALL_QUERY, new FilmRowMapper());
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Collection<Film> getRecommendedFilms(Long id) {
        return findMany(FIND_RECOMMENDED_FILMS_QUERY, id, id, id);
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
        long id = insert(INSERT_QUERY,
                film.getName(), film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
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
}
