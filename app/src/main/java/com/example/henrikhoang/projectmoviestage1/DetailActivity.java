package com.example.henrikhoang.projectmoviestage1;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.henrikhoang.projectmoviestage1.databinding.ActivityDetailsBinding;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Film> {

    private ActivityDetailsBinding mDetailBinding;
    private static final String TAG = DetailActivity.class.getSimpleName();

    public static int BOOM = 0;
    private static final int REVIEW_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);


        Film film = Parcels.unwrap(getIntent().getParcelableExtra("film"));
        mDetailBinding.primaryMovieInfo.tvMovieTitle.setText(film.getTitle());
        mDetailBinding.primaryMovieInfo.tvRating.setText(String.valueOf(film.getVote()));
        mDetailBinding.primaryMovieInfo.tvReleaseDate.setText(film.getDate());
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500"+ film.getPosterPath())
                .into(mDetailBinding.primaryMovieInfo.ivMoviePoster);


        int movieId = film.getId();
        BOOM = movieId;

        int loaderId = REVIEW_LOADER_ID;

        LoaderManager.LoaderCallbacks<Film> callback = DetailActivity.this;
        Bundle bunderForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bunderForLoader, callback);


    }


    @Override
    public Loader<Film> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Film>(this) {

            Film film = null;
            @Override
            protected void onStartLoading() {
                if (film != null) {
                    deliverResult(film);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Film loadInBackground() {
                try {
                    URL reviewRequestURL = Network.buildReviewsURL(DetailActivity.this, BOOM);
                    String jsonMovieResponse = Network.getResponseFromHttpUrl(reviewRequestURL);
                    Film film = OpenMovieJsonUtils.getReviewFromJson(DetailActivity.this, jsonMovieResponse);
                    return film;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Film> loader, Film data) {
        try {
            Log.d(TAG, "Review: " + data.getAuthor()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<Film> loader) {

    }
}