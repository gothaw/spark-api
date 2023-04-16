package com.radsoltan.dao;

import com.radsoltan.exc.DaoException;
import com.radsoltan.model.Film;

import java.util.List;

public interface FilmDao {
    void add(Film film) throws DaoException;

    List<Film> findAll();

    Film findById(int id);
}
