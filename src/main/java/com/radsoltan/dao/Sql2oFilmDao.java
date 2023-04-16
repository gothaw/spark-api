package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oFilmDao implements FilmDao{
    private final Sql2o sql2o;

    public Sql2oFilmDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Film film) throws DaoException {
        String sql = "INSERT INTO films(title, release_year, imdb_url) VALUES (:title, :releaseYear, :imdbUrl)";
        try (Connection connection = sql2o.open()) {
            int id = (int) connection.createQuery(sql)
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
        return null;
    }
}
