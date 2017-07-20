package com.example.henrikhoang.projectmoviestage1.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.henrikhoang.projectmoviestage1.data.MovieContract;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;

import java.net.URL;

/**
 * Created by henrikhoang on 7/18/17.
 */

public class MovieSyncTask {
    synchronized public static void syncMovie (Context context) {
        try {
            URL movieRequestUrl = Network.buildURL(context, "popular");
            String jsonMovieResponse = Network.getResponseFromHttpUrl(movieRequestUrl);

            ContentValues[] movieValues = OpenMovieJsonUtils.getMovieContentValuesFromJson(context, jsonMovieResponse);
            if (movieValues != null && movieValues.length !=0 ) {
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
