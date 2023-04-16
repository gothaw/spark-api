package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oFilmDaoTest {

    private static Sql2oFilmDao dao;
    private static Connection connection;

    @BeforeAll
    static void setUp() {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oFilmDao(sql2o);
        // Keep connection open through entire test, so it's not wiped out
        connection = sql2o.open();
    }

    @AfterAll
    static void tearDown() {
        connection.close();
    }

    @Test
    void addingFilmSetsId() throws DaoException {
        Film film = new Film("The Matrix", 1999, "https://www.imdb.com/title/tt0133093/");
        int originalFilmId = film.getId();

        dao.add(film);

        assertNotEquals(originalFilmId, film.getId());
    }

    @Test
    void addedCoursesAreReturnedFromFindAll() throws DaoException {
        Film film = new Film("The Matrix", 1999, "https://www.imdb.com/title/tt0133093/");

        dao.add(film);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    void noFilmsReturnEmptyList() {
        assertEquals(0, dao.findAll().size());
    }
}