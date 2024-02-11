package com.example.egorov.model;

public class Movie {
    private final int id;
    private final String name;
    private final String year;
    private final String genre;
    private final String imageUrl;
    private final String posterUrl;
    private final String country;
    private final String description;

    public Movie(int id, String name, String year, String genre, String imageUrl, String posterUrl, String country, String description) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.posterUrl = posterUrl;
        this.country = country;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}

