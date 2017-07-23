package com.example.henrikhoang.projectmoviestage1;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.henrikhoang.projectmoviestage1.adapter.ReviewAdapter;
import com.example.henrikhoang.projectmoviestage1.adapter.TrailerAdapter;
import com.example.henrikhoang.projectmoviestage1.databinding.ActivityDetailsBinding;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Film>,
TrailerAdapter.TrailerAdapterOnClickHandler,
ReviewAdapter.ReviewAdapterOnClickHandler {

    private ActivityDetailsBinding mDetailBinding;
    private static final String TAG = DetailActivity.class.getSimpleName();

    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    public static int MOVIE_ID = 0;
    private static final int TRAILERS_LOADER_ID = 100;
    private static final int REVIEWS_LOADER_ID = 101;

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

        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailerList);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviewList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this, this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);


        MOVIE_ID = film.getId();


        LoaderManager.LoaderCallbacks<Film> callback = DetailActivity.this;
        Bundle bunderForLoader = null;
        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, bunderForLoader, callback);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, bunderForLoader, callback);


    }


    @Override
    public Loader<Film> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case TRAILERS_LOADER_ID: {
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
                            URL trailerRequestURL = Network.buildTrailersURL(DetailActivity.this, MOVIE_ID);

                            String jsonMovieResponse = Network.getResponseFromHttpUrl(trailerRequestURL);

                            Film film = OpenMovieJsonUtils.getTrailerFromJson(DetailActivity.this, jsonMovieResponse);

                            mTrailerAdapter.setTrailerData(film);
                            mTrailerAdapter.notifyDataSetChanged();

                            return film;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            case REVIEWS_LOADER_ID: {
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
                            URL reviewRequestURL = Network.buildReviewsURL(DetailActivity.this, MOVIE_ID);
                            String jsonReviewResponse = Network.getResponseFromHttpUrl(reviewRequestURL);
                            Film review = OpenMovieJsonUtils.getReviewFromJson(DetailActivity.this, jsonReviewResponse);

                            mReviewAdapter.setReviewData(review);
                            mReviewAdapter.notifyDataSetChanged();
                            return review;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
            
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Film> loader, Film data) {

        if (null == data.getTrailerId() & data.getAuthor() == null) {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Film> loader) {

    }

    @Override
    public void onClick(String youtubeId) {
        Log.d(TAG, "YouTube id: " + youtubeId);
        Class destinationClass = OpenTrailerActivity.class;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(this, destinationClass);
        intent.putExtra("youtubeId", youtubeId);
        startActivity(intent);
    }


}