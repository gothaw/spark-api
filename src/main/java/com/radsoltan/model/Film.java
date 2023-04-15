package com.radsoltan.model;

import java.util.Objects;

public class Film {
    private int id;
    private String title;
    private int releaseYear;
    private String imdbUrl;

    public Film(String title, int releaseYear, String imdbUrl) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.imdbUrl = imdbUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && releaseYear == film.releaseYear && title.equals(film.title) && imdbUrl.equals(film.imdbUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, releaseYear, imdbUrl);
    }
}
