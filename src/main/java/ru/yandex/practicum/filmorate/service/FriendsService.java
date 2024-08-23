package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.user.FriendsStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
@Slf4j
public class FriendsService {

    private final FriendsStorage friendsStorage;

    public FriendsService(FriendsStorage friendsStorage) {
        this.friendsStorage = friendsStorage;
    }

    public Collection<Long> getUsersFriendsById(Long id) {
        return friendsStorage.findFriendsByUserId(id);
    }

    public void addFriend(long userId, long friendId, boolean friendship) {
        friendsStorage.addFriend(userId, friendId, friendship);
    }

    public void updateFriendship(long friendId, long id, boolean friendship) {
        friendsStorage.updateFriendship(friendId, id, friendship);
    }

    public void deleteFriend(long userId, long friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getAllFriends(long id) {
        return friendsStorage.getAllFriends(id);
    }
}