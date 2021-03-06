package com.example.henrikhoang.projectmoviestage1.utility;


import android.content.Context;

import com.example.henrikhoang.projectmoviestage1.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrikhoang on 5/29/17.
 */

public final class OpenMovieJsonUtils {
    private static final String RESULT = "results";
    final static  String TITLE = "original_title";
    final static String RELEASE_DATE = "release_date";
    final static String VOTE = "vote_average";
    final static String PLOT = "overview";
    final static String POSTER = "poster_path";
    final static String ID = "id";
    final static String AUTHOR = "author";
    final static String CONTENT = "content";
    final static String YOUTUBE_KEY = "key";
    final static String URL = "url";

    public static List<Film> getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);

//        title, release date, movie poster, vote average, and plot synopsis.

        JSONArray movieArray = movieJson.getJSONArray(RESULT);
        
        List<Film> films = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {


            JSONObject selectedMovie = movieArray.getJSONObject(i);

            Film tempFilm = new Film();
            tempFilm.setTitle(selectedMovie.getString(TITLE));
            tempFilm.setDate(selectedMovie.getString(RELEASE_DATE));
            tempFilm.setOverview(selectedMovie.getString(PLOT));
            tempFilm.setVote(selectedMovie.getDouble(VOTE));
            tempFilm.setPosterPath(selectedMovie.getString(POSTER));
            tempFilm.setId(selectedMovie.getInt(ID));

            films.add(tempFilm);

        }

        return films;

    }

    public static Film getReviewFromJson(Context context, String movieJsonStr)
        throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray reviewArray = movieJson.getJSONArray(RESULT);


        String[] authorData = new String[reviewArray.length()];
        String[] contentData = new String[reviewArray.length()];
        String[] urlData = new String[reviewArray.length()];
        Film film = new Film();

        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject review = reviewArray.getJSONObject(i);

            String author = review.getString(AUTHOR);
            String content = review.getString(CONTENT);
            String url = review.getString(URL);
            authorData[i] = author;
            contentData[i] = content;
            urlData[i] = url;

        }
        film.setComment(contentData);
        film.setAuthor(authorData);
        film.setUrlReview(urlData);

        return film;
    }

    public static Film getTrailerFromJson(Context context, String movieJsonStr)
        throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray videoArray = movieJson.getJSONArray(RESULT);

        String[] trailerData = new String[videoArray.length()];
        Film film = new Film();

        for (int i = 0; i < videoArray.length(); i++) {
            JSONObject trailer = videoArray.getJSONObject(i);
            String key = trailer.getString(YOUTUBE_KEY);
            trailerData[i] = key;
        }
        film.setTrailerId(trailerData);
        return film;

    }


}

