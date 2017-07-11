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

            films.add(tempFilm);

        }

        return films;

    }

}

