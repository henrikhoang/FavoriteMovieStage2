package com.example.henrikhoang.projectmoviestage1.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.henrikhoang.projectmoviestage1.R;

/**
 * Created by henrikhoang on 7/11/17.
 */

public class MoviePreferences {
    public static String getPreferredSort(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSort = context.getString(R.string.pref_sortby_popular_label);
        String defaultSort = context.getString(R.string.pref_sortby_popular_value);
        return prefs.getString(keyForSort, defaultSort);
    }
}
