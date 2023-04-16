package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql2oFilmDao implements FilmDao{
    private final Sql2o sql2o;

    public Sql2oFilmDao(Sql2o sql2o) {
        this.sql2o = sql2o;
        Map<String, String> columnMaps = new HashMap<String, String>(Map.of("RELEASE_YEAR", "releaseYear", "IMDB_URL", "imdbUrl"));

        sql2o.setDefaultColumnMappings(columnMaps);
    }

    @Override
    public void add(Film film) throws DaoException {
        String sql = "INSERT INTO films(title, release_year, imdb_url) VALUES (:title, :releaseYear, :imdbUrl)";
        try (Connection connection = sql2o.open()) {
            int id = (int) connection.createQuery(sql)
                    .addParameter("title", film.getTitle())
                    .addParameter("releaseYear", film.getReleaseYear())
                    .addParameter("imdbUrl", film.getImdbUrl())
                    .bind(film)
                    .executeUpdate()
                    .getKey();

            film.setId(id);
        } catch (Sql2oException exception) {
            throw new DaoException(exception, "Problem adding film");
        }
    }

    @Override
    public List<Film> findAll() {
        Connection connection = sql2o.open();

        return connection.createQuery("SELECT * FROM films")
                    .executeAndFetch(Film.class);
    }
}
