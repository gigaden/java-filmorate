package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Repository("likeStorage")
public class LikeStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_LIKES_BY_FILM_ID = "SELECT users_id FROM likes WHERE films_id = ?";
    private static final String INSERT_LIKES_FROM_USERS = "INSERT INTO likes(films_id, users_id) VALUES (?,?)";
    private static final String DELETE_BY_ID = "DELETE from likes WHERE films_id = ? AND users_id = ?";

    public LikeStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }


    public Collection<Long> findLikesByFilmId(long id) {
        return jdbc.queryForList(FIND_ALL_LIKES_BY_FILM_ID, Long.class, id);
    }

    public void addLike(Long id, Long userId) {
        add(INSERT_LIKES_FROM_USERS, id, userId);
    }

    public void delLike(Long id, Long userId) {
        delete(DELETE_BY_ID, id, userId);
    }


}
