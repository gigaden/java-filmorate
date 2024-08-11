package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Deprecated
@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Like like = new Like();
        like.setFilm_id(resultSet.getInt("films_id"));
        like.setUser_id(resultSet.getInt("users_id"));

        return like;
    }
}
