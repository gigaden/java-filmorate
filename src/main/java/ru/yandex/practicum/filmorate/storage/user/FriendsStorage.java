package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.BaseDbStorage;

import java.util.Collection;

@Repository("friendsStorage")
public class FriendsStorage extends BaseDbStorage<User> {

    private static final String FIND_ALL_FRIENDS_BY_USERS_ID = "SELECT friend_id FROM friends" +
            " WHERE user_id = ? AND friendship is TRUE";

    public FriendsStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Long> findFriendsByUserId(long id) {
        return jdbc.queryForList(FIND_ALL_FRIENDS_BY_USERS_ID, Long.class, id);
    }


}
