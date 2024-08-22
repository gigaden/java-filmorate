package ru.yandex.practicum.filmorate.dal.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ReviewDbStorage extends BaseDbStorage<Review> {
    private static final String INSERT_QUERY = """
            INSERT INTO reviews(content, is_positive, user_id, film_id) VALUES(?, ?, ?, ?)
            """;
    private static final String UPDATE_QUERY = """
            UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ? WHERE review_id = ?
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT * FROM reviews WHERE review_id = ?
            """;
    private static final String DELETE_BY_ID = """
            DELETE FROM reviews WHERE review_id = ?
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM reviews LIMIT ?
            """;
    private static final String FIND_BY_ID_FILM_QUERY = """
            SELECT * FROM reviews WHERE film_id = ? LIMIT ?
            """;
    private static final String INSERT_LIKE_QUERY = """
            INSERT INTO useful(review_id, user_id, useful) VALUES(?, ?, ?)
            """;
    private static final String DELETE_LIKE_BY_ID = """
            DELETE FROM useful WHERE review_id = ? AND user_id = ? AND useful = ?
            """;
    private static final String DELETE_LIKE_BY_ID_NO_USEFUL = """
            DELETE FROM useful WHERE review_id = ? AND user_id = ?
            """;
    private static final String FIND_LIKE_COUNT_QUERY = """
            SELECT review_id FROM useful WHERE review_id = ? AND useful = true
            """;
    private static final String FIND_DISLIKE_COUNT_QUERY = """
            SELECT review_id FROM useful WHERE review_id = ? AND useful = false
            """;

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review create(Review review) {
        log.info("Создания отзыва в базе данных: {}", review);
        long reviewId = insert(INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());
        review.setReviewId(reviewId);
        review.setUseful(0);
        log.info("Отзыв в базе данных создан: {}", get(reviewId));
        return review;
    }

    public Review update(Review review) {
        log.info("Попытка обновить отзыв в базе данных: {}", get(review.getReviewId()));
        update(UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId());
        log.info("Отзыв в базе данных обнавлен: {}", review);
        return review;
    }

    public Optional<Review> get(Long reviewId) {
        return findOne(FIND_BY_ID_QUERY, reviewId);
    }

    public void delete(Long reviewId) {
        log.info("Попытка удалить отзыв из базы данных: {}", get(reviewId));
        delete(DELETE_BY_ID, reviewId);
        log.info("Отзыв с id: {} успешно удален из базы данных", reviewId);
    }

    public Collection<Review> getAll(int count) {
        log.info("Попытка получения коллекции отзывов из базы данных, count: {}", count);
        Collection<Review> reviewCollection = findMany(FIND_ALL_QUERY, count);
        log.info("Коллекция отзывов из базы данных успешно получена");
        return reviewCollection;
    }

    public Collection<Review> getAllOfFilm(Long filmId, int count) {
        log.info("Попытка получения коллекции отзывов фильма из базы данных, filmId: {}, count: {}", filmId, count);
        Collection<Review> reviewCollection = findMany(FIND_BY_ID_FILM_QUERY, filmId, count);
        log.info("Коллекция отзывов фильма успешно получена из базы данных");
        return reviewCollection;
    }

    public void createUseful(Long reviewId, Long userId, boolean useful) {
        log.info("Добавление лака/дизлайка в базе данных reviewId: {}, userId: {}, useful: {}", reviewId, userId, useful);
        delete(DELETE_LIKE_BY_ID_NO_USEFUL, reviewId, userId);
        add(INSERT_LIKE_QUERY, reviewId, userId, useful);
        log.info("Лайк/дизлайк успешно добавлен reviewId: {}, userId: {}, useful: {}", reviewId, userId, useful);
    }

    public void removeUseful(Long reviewId, Long userId, boolean useful) {
        delete(DELETE_LIKE_BY_ID, reviewId, userId, useful);
    }

    public int findUsefulCount(Long reviewId) {
        List<Long> likeList = new ArrayList<>(findManyInstances(FIND_LIKE_COUNT_QUERY, Long.class, reviewId));
        List<Long> dislikeList = new ArrayList<>(findManyInstances(FIND_DISLIKE_COUNT_QUERY, Long.class, reviewId));
        return likeList.size() - dislikeList.size();
    }
}