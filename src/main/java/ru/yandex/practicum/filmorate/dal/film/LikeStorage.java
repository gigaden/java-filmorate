package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Repository("likeStorage")
public class LikeStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_LIKES_BY_FILM_ID = "SELECT users_id FROM likes WHERE films_id = ?";
    private static final String INSERT_LIKES_FROM_USERS = "INSERT INTO likes(films_id, users_id) VALUES (?,?)";
    private static final String DELETE_BY_ID = "DELETE from likes WHERE films_id = ? AND users_id = ?";

    public LikeStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }


    // Получаем id юзеров, лайкнувших фильм
    public Collection<Long> findLikesByFilmId(long id) {
        return jdbc.queryForList(FIND_ALL_LIKES_BY_FILM_ID, Long.class, id);
    }

    // Добавляем фильму лайк через сводную таблицу
    public void addLike(Long id, Long userId) {
        add(INSERT_LIKES_FROM_USERS, id, userId);
    }

    // Удаляем лайк у фильма
    public void delLike(Long id, Long userId) {
        delete(DELETE_BY_ID, id, userId);
    }


}
