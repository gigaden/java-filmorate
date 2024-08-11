package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository("mpaStorage")
public class MpaStorage extends BaseDbStorage<Mpa> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpas WHERE id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM mpas";

    public MpaStorage(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Collection<Mpa> getAll() {
        return findMany(GET_ALL_QUERY);
    }
}
