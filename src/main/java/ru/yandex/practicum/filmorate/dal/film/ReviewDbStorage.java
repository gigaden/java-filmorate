package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseDbStorage<Review> {
    private static final String INSERT_QUERY = "INSERT INTO reviews(content, is_positive, user_id, film_id) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ? WHERE review_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM reviews WHERE review_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM reviews WHERE review_id = ?";
    private static final String FIND_ALL_QUERY = " SELECT * FROM reviews LIMIT ?";
    private static final String FIND_BY_ID_FILM_QUERY = " SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
    private static final String INSERT_LIKE_QUERY = " INSERT INTO useful(review_id, user_id, useful) VALUES(?, ?, ?)";
    private static final String DELETE_LIKE_BY_ID = "DELETE FROM useful WHERE review_id = ? AND user_id = ? AND useful = ?";
    private static final String DELETE_LIKE_BY_ID_NO_USEFUL = "DELETE FROM useful WHERE review_id = ? AND user_id = ?";
    private static final String FIND_LIKE_COUNT_QUERY = " SELECT * FROM useful WHERE review_id = ? AND useful = true";
    private static final String FIND_DISLIKE_COUNT_QUERY = "SELECT * FROM useful WHERE review_id = ? AND useful = false";

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    public Review create(Review review) {
        long reviewId = insert(INSERT_QUERY, review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId());
        review.setReviewId(reviewId);
        review.setUseful(0);
        return review;
    }

    public Review update(Review review) {
        update(UPDATE_QUERY, review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId(), review.getReviewId());
        return review;
    }

    public Optional<Review> get(Long reviewId) {
        return findOne(FIND_BY_ID_QUERY, reviewId);
    }

    public void delete(Long reviewId) {
        delete(DELETE_BY_ID, reviewId);
    }

    public Collection<Review> getAll(int count) {
        return findMany(FIND_ALL_QUERY, count);
    }

    public Collection<Review> getAllOfFilm(Long filmId, int count) {
        return findMany(FIND_BY_ID_FILM_QUERY, filmId, count);
    }

    public void createUseful(Long reviewId, Long userId, boolean useful) {
        delete(DELETE_LIKE_BY_ID_NO_USEFUL, reviewId, userId);
        add(INSERT_LIKE_QUERY, reviewId, userId, useful);
    }

    public void removeUseful(Long reviewId, Long userId, boolean useful) {
        delete(DELETE_LIKE_BY_ID, reviewId, userId, useful);
    }

    public int findUsefulCount(Long reviewId) {
        SqlRowSet sqlRowSetLike = jdbc.queryForRowSet(FIND_LIKE_COUNT_QUERY, reviewId);
        int likeCount = 0;
        while (sqlRowSetLike.next()) {
            likeCount++;
        }
        SqlRowSet sqlRowSetDislike = jdbc.queryForRowSet(FIND_DISLIKE_COUNT_QUERY, reviewId);
        int dislikeCount = 0;
        while (sqlRowSetDislike.next()) {
            dislikeCount++;
        }
        return likeCount - dislikeCount;
    }
}