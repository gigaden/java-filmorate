package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;

import java.util.Collection;

@Service
public class LikeService {
    LikeStorage likeStorage;

    public LikeService(LikeStorage likeStorage) {
        this.likeStorage = likeStorage;
    }

    public Collection<Long> getLikesByFilmId(Long id) {
        return likeStorage.findLikesByFilmId(id);
    }

    public void addLike(Long id, Long userId) {
        likeStorage.addLike(id, userId);
    }

    public void delLike(Long id, Long userId) {
        likeStorage.delLike(id, userId);
    }
}
