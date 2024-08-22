package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
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
    private static final String FIND_FILMS_BY_DIRECTOR_ID = "SELECT * FROM film_director WHERE director_id = ?";
    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }


    // Получаем все фильмы
    @Override
    public Collection<Film> getAll() {
        //return jdbc.query(FIND_ALL_QUERY, new FilmRowMapper());
        return findMany(FIND_ALL_QUERY);
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

    // Получаем список фильмов по id режиссера
    public Collection<Film> getAllFilmsByDirectorId(Long id) {
        return findMany(FIND_FILMS_BY_DIRECTOR_ID, id);
    }
}
