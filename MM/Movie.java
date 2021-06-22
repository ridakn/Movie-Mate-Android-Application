//picture
//name
//id
//intro


package com.coen268.moviemate;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Movie} object contains information related to a single earthquake.
 */
public class Movie {

    public static final String LOG_TAG = Movie.class.getSimpleName();

    long id;
    String title;
    String overview;
    String releaseDate;
    String posterPath;
    String score;
    String trailerPath;

    List<String> subscribeAvailable;
//    String Netflix;
//    String Amazon;
//    String Hulu;
//    String Youtube;



    public long getId() {
        return id;
    }

    public long getIdFromTitle(String name) { return id; }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public String getScore() { return score; }

    public List<String> getSubscribeAvailable() {
        return subscribeAvailable;
    }

    /**
     * Constructs a new {@link Movie} object.
     *
     */
    public Movie(long id,
                 String title,
                 String overview,String releaseDate,
                 String posterPath,
                 String trailerPath,
                 String scores,
                 List<String> subscribeAvailable) {

        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.trailerPath = trailerPath;
        this.score = scores;
        this.subscribeAvailable = subscribeAvailable;
    }

}