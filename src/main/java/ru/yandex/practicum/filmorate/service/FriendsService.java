package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.FriendsStorage;

import java.util.Collection;

@Service
@Slf4j
public class FriendsService {

    private final FriendsStorage friendsStorage;

    public FriendsService(FriendsStorage friendsStorage) {
        this.friendsStorage = friendsStorage;
    }

    public Collection<Long> getUsersFriendsById (Long id) {
        return friendsStorage.findFriendsByUserId(id);
    }
}
