package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;
import com.radsoltan.model.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oReviewDaoTest {

    private Film film;
    private Connection connection;
    private Sql2oFilmDao filmDao;
    private Sql2oReviewDao reviewDao;

    @BeforeEach
    void setUp() throws DaoException {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        connection = sql2o.open();
        filmDao = new Sql2oFilmDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        film = newTestFilm();
        filmDao.add(film);
    }

    @AfterEach
    void tearDown() {
        connection.close();
    }

    @Test
    void addingNewReviewSetsId() throws DaoException {
        Review review = new Review(film.getId(), 5, "Pretty good.");
        int originalReviewId = review.getId();

        reviewDao.add(review);

        assertNotEquals(originalReviewId, review.getId());
    }

    @Test
    void addedReviewsAreReturnedFromFindAll() throws DaoException {
        Review review = new Review(film.getId(), 5, "Pretty good.");

        reviewDao.add(review);

        assertEquals(1, reviewDao.findAll().size());
    }

    @Test
    void noReviewsReturnEmptyList() {
        assertEquals(0, reviewDao.findAll().size());
    }

    @Test
    void multipleReviewsAreFoundWhenTheyExistForAFilm() throws DaoException {
        reviewDao.add(new Review(film.getId(), 5, "Pretty good."));
        reviewDao.add(new Review(film.getId(), 3, "Alright."));
        reviewDao.add(new Review(film.getId(), 1, "Bad!"));

        List<Review> foundReviews = reviewDao.findByFilmId(film.getId());

        assertEquals(3, foundReviews.size());
    }

    @Test
    void addingAReviewToANonExistingFilmFails() {
        Review review = new Review(42, 5, "Pretty good.");

        assertThrows(DaoException.class, () -> reviewDao.add(review));
    }

    private static Film newTestFilm() {
        return new Film("The Matrix", 1999, "https://www.imdb.com/title/tt0133093/");
    }
}