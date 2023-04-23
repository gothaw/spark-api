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
    private final Map<String, String> columnMaps;

    public Sql2oFilmDao(Sql2o sql2o) {
        this.sql2o = sql2o;
        columnMaps = new HashMap<>(Map.of("RELEASE_YEAR", "releaseYear", "IMDB_URL", "imdbUrl"));
    }

    @Override
    public void add(Film film) throws DaoException {
        String sql = "INSERT INTO films(title, release_year, imdb_url) VALUES (:title, :releaseYear, :imdbUrl)";
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            int id = (int) connection
                    .createQuery(sql)
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
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM films")
                    .executeAndFetch(Film.class);
        }
    }

    @Override
    public Film findById(int id) {
        sql2o.setDefaultColumnMappings(columnMaps);

        try (Connection connection = sql2o.open()) {
            return connection
                    .createQuery("SELECT * FROM films WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Film.class);
        }
    }
}
