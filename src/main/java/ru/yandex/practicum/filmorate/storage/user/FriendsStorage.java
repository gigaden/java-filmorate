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
            " WHERE user_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends(user_id,friend_id,friendship)" +
            "VALUES (?,?,?)";
    private static final String UPDATE_FRIENDSHIP = "UPDATE friends SET friendship = ?" +
            "WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_FRIENDS_FROM_USER = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String GET_ALL_USERS_FRIENDS = "SELECT * FROM users u" +
            " JOIN friends f ON u.id = f.friend_id AND f.user_id = ?";

    public FriendsStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Long> findFriendsByUserId(long id) {
        return jdbc.queryForList(FIND_ALL_FRIENDS_BY_USERS_ID, Long.class, id);
    }

    public void addFriend(long userId, long friendId, boolean friendship) {
        add(ADD_FRIEND_QUERY, friendId, userId, friendship);
    }

    public void updateFriendship(long userId, long friendId, boolean friendship) {
        update(UPDATE_FRIENDSHIP,
                friendship,
                userId,
                friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        delete(DELETE_FRIENDS_FROM_USER, userId, friendId);
    }

    public Collection<User> getAllFriends(long id) {
        return findMany(GET_ALL_USERS_FRIENDS, id);
    }


}
