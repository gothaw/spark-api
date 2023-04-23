package com.radsoltan;

import com.google.gson.Gson;
import com.radsoltan.dao.Sql2oFilmDao;
import com.radsoltan.dao.Sql2oReviewDao;
import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;
import com.radsoltan.model.Review;
import com.radsoltan.testing.ApiClient;
import com.radsoltan.testing.ApiResponse;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    public static final String PORT = "4568";
    public static final String TEST_DATA_SOURCE = "jdbc:h2:mem:testing;";
    private Connection connection;
    private ApiClient client;
    private Gson gson;
    private Sql2oFilmDao filmDao;
    private Sql2oReviewDao reviewDao;

    @BeforeAll
    static void startServer() {
        String[] args = {PORT, TEST_DATA_SOURCE};
        Api.main(args);
    }

    @AfterAll
    static void stopServer() {
        Spark.stop();
    }

    @BeforeEach
    void setUp() {
        Sql2o sql2o = new Sql2o(TEST_DATA_SOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        filmDao = new Sql2oFilmDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        connection = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @AfterEach
    void tearDown() {
        connection.close();
    }

    @Test
    void addingFilmsReturnsCreatedStatus() {
       Map<String, Object> values = new HashMap<>();
       values.put("title", "Test Film 2");
       values.put("releaseYear", 2020);
       values.put("imdbUrl", "https://test.com/film/test2");

       ApiResponse response = client.request("POST", "/films", gson.toJson(values));

       assertEquals(201, response.getStatus());
    }

    @Test
    void filmsCanBeAccessedById() throws DaoException {
        Film film = newTestFilm();
        filmDao.add(film);

        ApiResponse response = client.request("GET", "/films/" + film.getId());

        Film retrievedFilm = gson.fromJson(response.getBody(), Film.class);

        assertEquals(film, retrievedFilm);
    }

    @Test
    void missingFilmsReturnNotFoundStatus() {
        ApiResponse response = client.request("GET", "/films/42");

        assertEquals(404, response.getStatus());
    }

    @Test
    void addingReviewsReturnsCreatedStatus() throws DaoException {
        Film film = newTestFilm();
        filmDao.add(film);
        Map<String, Object> values = new HashMap<>();
        values.put("rating", 5);
        values.put("comment", "Pretty good.");

        ApiResponse response = client.request("POST", String.format("/films/%d/reviews", film.getId()), gson.toJson(values));

        assertEquals(201, response.getStatus());
    }

    @Test
    void addingReviewToUnknownFilmThrowsError() {
        Map<String, Object> values = new HashMap<>();
        values.put("rating", 5);
        values.put("comment", "Pretty good.");

        ApiResponse response = client.request("POST", "/films/42/reviews", gson.toJson(values));

        assertEquals(500, response.getStatus());
    }

    @Test
    void reviewsCanBeAccessedById() throws DaoException {
        Film film = newTestFilm();
        filmDao.add(film);
        reviewDao.add(new Review(film.getId(), 5, "Very good."));
        reviewDao.add(new Review(film.getId(), 1, "Bad."));
        reviewDao.add(new Review(film.getId(), 3, "Alright."));

        ApiResponse response = client.request("GET", String.format("/films/%d/reviews", film.getId()));

        Review[] retrievedReviews = gson.fromJson(response.getBody(), Review[].class);

        assertEquals(3, retrievedReviews.length);
    }

    private static Film newTestFilm() {
        return new Film("The Matrix", 1999, "https://www.imdb.com/title/tt0133093/");
    }
}