package com.example.henrikhoang.projectmoviestage1.utility;


import android.content.ContentValues;
import android.content.Context;

import com.example.henrikhoang.projectmoviestage1.Film;
import com.example.henrikhoang.projectmoviestage1.data.MovieContract;

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

    public static ContentValues[] getMovieContentValuesFromJson (Context context, String movieJsonStr)
    throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(RESULT);
        ContentValues[] movieContentValues = new ContentValues[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String title,date, plot, poster;
            int vote, id;

            JSONObject selectedMovie = movieArray.getJSONObject(i);
            title = selectedMovie.getString(TITLE);
            date = selectedMovie.getString(RELEASE_DATE);
            plot = selectedMovie.getString(PLOT);
            poster = selectedMovie.getString(POSTER);

            vote = selectedMovie.getInt(VOTE);
            id = selectedMovie.getInt(ID);

            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, poster);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, plot);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, date);
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE, vote);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);

            movieContentValues[i] = movieValues;

        }
        return movieContentValues;
    }

}

