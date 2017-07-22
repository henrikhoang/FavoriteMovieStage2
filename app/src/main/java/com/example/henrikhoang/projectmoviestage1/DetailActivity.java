package com.example.henrikhoang.projectmoviestage1;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.henrikhoang.projectmoviestage1.adapter.TrailerAdapter;
import com.example.henrikhoang.projectmoviestage1.databinding.ActivityDetailsBinding;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Film>,
TrailerAdapter.TrailerAdapterOnClickHandler {

    private ActivityDetailsBinding mDetailBinding;
    private static final String TAG = DetailActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TrailerAdapter mTrailerAdapter;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailerList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mRecyclerView.setAdapter(mTrailerAdapter);

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
                    URL reviewRequestURL = Network.buildTrailersURL(DetailActivity.this, BOOM);
                    String jsonMovieResponse = Network.getResponseFromHttpUrl(reviewRequestURL);
                    Film film = OpenMovieJsonUtils.getTrailerFromJson(DetailActivity.this, jsonMovieResponse);
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
        mTrailerAdapter.setTrailerData(data);
        if (null == data.getTrailerId()) {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Film> loader) {

    }

    @Override
    public void onClick(String youtubeId) {
        Context context = this;
        Class desticationClass = OpenTrailerIntent.class;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(context, desticationClass);
        intent.putExtra("youtubeId", youtubeId);
        startActivity(intent);
    }
}