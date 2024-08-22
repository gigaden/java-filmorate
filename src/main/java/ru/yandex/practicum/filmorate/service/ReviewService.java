package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.ReviewDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewDbStorage reviewDbStorage;
    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage, FilmService filmService, UserService userService) {
        this.reviewDbStorage = reviewDbStorage;
        this.filmService = filmService;
        this.userService = userService;
    }

    public Review create(Review review) {
        log.info("Попытка создать отзыв");
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
        reviewDbStorage.create(review);
        log.info("Отзыв с id: {} успешно добавлен", review.getReviewId());
        return review;
    }

    public Review update(Review review) {
        log.info("Попытка обновить отзыв");
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
        reviewDbStorage.update(review);
        log.info("Отзыв с id: {} успешно обновлен", review.getReviewId());
        return review;
    }

    public Review get(Long reviewId) {
        log.info("Попытка получить отзыв по id: {}", reviewId);
        Review review = reviewDbStorage.get(reviewId).orElseThrow(() -> new NotFoundException("Отзыв c id:" + reviewId + " не найден"));
        review.setUseful(reviewDbStorage.findUsefulCount(review.getReviewId()));
        log.info("Отзыв с id: {} успешно получен", review.getReviewId());
        return review;
    }

    public void delete(Long reviewId) {
        log.info("Попытка удалить отзыв с id: {}", reviewId);
        get(reviewId);
        reviewDbStorage.delete(reviewId);
        log.info("Отзыв с id: {} успешно удален", reviewId);
    }

    public Collection<Review> getAllOfParam(Long filmId, int count) {
        log.info("Попытка получить фильмы");
        if (filmId > 0) {
            log.info("Попытка получить отзывы фильма filmId: {}, count: {}", filmId, count);
            return setUseful(reviewDbStorage.getAllOfFilm(filmId, count));
        } else {
            log.info("Попытка получить отзывы всех фильмов count: {}", count);
            return setUseful(reviewDbStorage.getAll(count));
        }
    }

    public void createUseful(Long reviewId, Long userId, boolean useful) {
        log.info("Попытка создать оценку отзыву");
        reviewDbStorage.createUseful(reviewId, userId, useful);
        log.info("Оцунка успешно создана");
    }

    public void removeUseful(Long reviewId, Long userId, boolean useful) {
        log.info("Попытка удалить оценку");
        reviewDbStorage.removeUseful(reviewId, userId, useful);
        log.info("Оцунка успешно удалена");
    }

    public List<Review> setUseful(Collection<Review> reviews) {
        List<Review> usefulReviews = new ArrayList<>(reviews.size());
        for (Review rew : reviews) {
            rew.setUseful(reviewDbStorage.findUsefulCount(rew.getReviewId()));
            usefulReviews.add(rew);
        }
        return usefulReviews;
    }
}