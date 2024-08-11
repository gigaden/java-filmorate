package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
//@RequiredArgsConstructor
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films f JOIN mpas m ON f.mpa = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate," +
            "duration, mpa)" +
            "VALUES (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?," +
            " releaseDate = ?, duration = ?, mpa = ? WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE from films WHERE id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }


    @Override
    public Collection<Film> getAll() {
        //return jdbc.query(FIND_ALL_QUERY, new FilmRowMapper());
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Film> get(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Film create(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(), film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(), film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_BY_ID, id);
    }
}
