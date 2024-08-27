package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.RatingStorage;

import java.util.Collection;

@Service
public class RatingService {
    RatingStorage ratingStorage;

    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Collection<Long> findUsersByFilmRating(Long id) {
        return ratingStorage.findUsersByFilmRating(id);
    }

    public void addRatingByFilm(Long id, Long userId, Integer rating) {
        ratingStorage.addRatingByFilm(id, userId, rating);
    }

    public void deleteRatingByFilm(Long id, Long userId) {
        ratingStorage.deleteRatingByFilm(id, userId);
    }
}