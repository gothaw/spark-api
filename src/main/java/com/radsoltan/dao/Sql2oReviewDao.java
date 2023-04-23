package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql2oReviewDao implements ReviewDao {
    private final Sql2o sql2o;
    private final Map<String, String> columnMaps;

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;

        columnMaps = new HashMap<>(Map.of("FILM_ID", "filmId", "RATING", "rating", "COMMENT", "comment"));
    }

    @Override
    public void add(Review review) throws DaoException {
        String sql = "INSERT INTO reviews(film_id, rating, comment) VALUES (:filmId, :rating, :comment)";
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            int id = (int) connection
                    .createQuery(sql)
                    .addParameter("filmId", review.getFilmId())
                    .addParameter("rating", review.getRating())
                    .addParameter("comment", review.getComment())
                    .bind(review)
                    .executeUpdate()
                    .getKey();

            review.setId(id);
        } catch (Sql2oException exception) {
            throw new DaoException(exception, "Problem adding review");
        }
    }

    @Override
    public List<Review> findAll() {
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM reviews")
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> findByFilmId(int filmId) {
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM reviews WHERE film_id = :filmId")
                    .addParameter("filmId", filmId)
                    .executeAndFetch(Review.class);
        }
    }
}
