package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Review;

import java.util.List;

public interface ReviewDao {
    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findByFilmId(int filmId);
}
