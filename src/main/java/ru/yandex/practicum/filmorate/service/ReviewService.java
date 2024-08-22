package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.film.ReviewDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewDbStorage reviewDbStorage;
    private final FilmService filmService;
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public ReviewService(ReviewDbStorage reviewDbStorage, FilmService filmService, UserService userService,
                         EventService eventService) {
        this.reviewDbStorage = reviewDbStorage;
        this.filmService = filmService;
        this.userService = userService;
        this.eventService = eventService;
    }

    public Review create(Review review) {
        log.info("Попытка создать отзыв {}", review);
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
        reviewDbStorage.create(review);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, Operation.ADD, review.getReviewId());
        log.info("Отзыв успешно добавлен {}", get(review.getReviewId()));
        return review;
    }

    public Review update(Review review) {
        log.info("Попытка обновить отзыв {}", get(review.getReviewId()));
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());
        reviewDbStorage.update(review);
        eventService.createEvent(review.getUserId(), EventType.REVIEW, Operation.UPDATE, review.getReviewId());
        log.info("Отзыв успешно обновлен {}", review);
        return review;
    }

    public Review get(Long reviewId) {
        log.info("Попытка получить отзыв по id: {}", reviewId);
        Review review = reviewDbStorage.get(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв c id:" + reviewId + " не найден"));
        review.setUseful(reviewDbStorage.findUsefulCount(review.getReviewId()));
        log.info("Отзыв успешно получен {}", review);
        return review;
    }

    public void delete(Long reviewId) {
        log.info("Попытка удалить отзыв {}", get(reviewId));
        Long id = get(reviewId).getUserId();
        eventService.createEvent(id, EventType.REVIEW, Operation.REMOVE, reviewId);
        reviewDbStorage.delete(reviewId);
        log.info("Отзыв с id: {} успешно удален", reviewId);
    }

    public Collection<Review> getAllOfParam(Long filmId, int count) {
        if (filmId > 0) {
            log.info("Попытка получить отзывы фильма filmId: {}, count: {}", filmId, count);
            Collection<Review> reviewCollection = setUseful(reviewDbStorage.getAllOfFilm(filmId, count));
            log.info("Отзывы фильма получены filmId: {}, count: {}", filmId, count);
            return reviewCollection;
        } else {
            log.info("Попытка получить отзывы всех фильмов count: {}", count);
            Collection<Review> reviewCollection = setUseful(reviewDbStorage.getAll(count));
            log.info("Отзывы всех фильмов получены count: {}", count);
            return reviewCollection;
        }
    }

    public void createUseful(Long reviewId, Long userId, boolean useful) {
        log.info("Попытка создать оценку отзыву {}", get(reviewId));
        reviewDbStorage.createUseful(reviewId, userId, useful);
        log.info("Оцунка успешно создана {}", get(reviewId));
    }

    public void removeUseful(Long reviewId, Long userId, boolean useful) {
        log.info("Попытка удалить оценку {}", get(reviewId));
        reviewDbStorage.removeUseful(reviewId, userId, useful);
        log.info("Оцунка c id: {} успешно удалена", reviewId);
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