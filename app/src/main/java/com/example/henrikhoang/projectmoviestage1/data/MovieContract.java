package com.example.henrikhoang.projectmoviestage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by henrikhoang on 7/11/17.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.henrikhoang.projectmoviestage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieEntry implements BaseColumns {




        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_MOVIE_ID = "id";

        public static Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();
    }
}
