package com.example.henrikhoang.projectmoviestage1.utility;

import android.content.Context;
import android.net.Uri;

import com.example.henrikhoang.projectmoviestage1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by henrikhoang on 5/26/17.
 */

public final class Network {

    private static final String BASE_MOVIE_URL =
            "https://api.themoviedb.org/3/movie";

    final static int NUM_PAGE = 1;
    final static String PAGE_PARAM = "page";
    final static String LANG_PARAM = "original_language";
    final static String language = "en";
    final static String api = "api_key";
    private static final String MOVIE_BASE_URL = BASE_MOVIE_URL;


    public static String getApiKey(Context context) {
        String api_key = context.getString(R.string.API_KEY);
        return api_key;
    }


    public static URL buildURL(Context context, String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(api, getApiKey(context))
                .appendQueryParameter(PAGE_PARAM, Integer.toString(NUM_PAGE))
                .appendQueryParameter(LANG_PARAM, language)
                .build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailersURL(Context context, int id) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(context.getString(R.string.video_trailers))
                .appendQueryParameter(api, getApiKey(context))
                .build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsURL(Context context, int id) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(context.getString(R.string.reviews))
                .appendQueryParameter(api, getApiKey(context))
                .build();

        URL url = null;
        try
        {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
