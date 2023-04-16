CREATE TABLE IF NOT EXISTS films (
   id INTEGER PRIMARY KEY auto_increment,
   title VARCHAR,
   release_year INTEGER,
   imdb_url VARCHAR
);

CREATE TABLE IF NOT EXISTS reviews (
   id INTEGER PRIMARY KEY auto_increment,
   film_id INTEGER,
   rating INTEGER,
   comment VARCHAR,
   FOREIGN KEY(film_id) REFERENCES public.films(id)
);