package com.radsoltan;

import com.google.gson.Gson;
import com.radsoltan.dao.FilmDao;
import com.radsoltan.dao.Sql2oFilmDao;
import com.radsoltan.model.Film;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Api {
    public static void main(String[] args) {
        String dataSource = "jdbc:h2:~/reviews.db";
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("java API <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            dataSource = args[1];
        }

        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", dataSource),
                "",
                ""
        );
        FilmDao filmDao = new Sql2oFilmDao(sql2o);
        Gson gson = new Gson();

        post("/films", "application/json", (request, response) -> {
            Film film = gson.fromJson(request.body(), Film.class);
            filmDao.add(film);
            response.status(201);
            return film;
        }, gson::toJson);

        get("/films", "application/json", (request, response) -> filmDao.findAll(), gson::toJson);

        get("/films/:id", "application/json", (request, response) -> {
            int id = Integer.parseInt(request.params("id"));

            // TODO: 16/04/2023 What if this is not found
            return filmDao.findById(id);
        }, gson::toJson);

        after((request, response) -> response.type("application/json"));
    }
}