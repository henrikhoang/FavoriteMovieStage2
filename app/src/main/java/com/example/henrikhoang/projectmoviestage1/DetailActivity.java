package com.example.henrikhoang.projectmoviestage1;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.henrikhoang.projectmoviestage1.adapter.ReviewAdapter;
import com.example.henrikhoang.projectmoviestage1.adapter.TrailerAdapter;
import com.example.henrikhoang.projectmoviestage1.data.MovieContract;
import com.example.henrikhoang.projectmoviestage1.databinding.ActivityDetailsBinding;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URL;

import static android.view.View.GONE;

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


        final Film film = Parcels.unwrap(getIntent().getParcelableExtra("film"));
        mDetailBinding.primaryMovieInfo.tvMovieTitle.setText(film.getTitle());
        mDetailBinding.primaryMovieInfo.tvRating.setText(String.valueOf(film.getVote()));
        mDetailBinding.primaryMovieInfo.tvReleaseDate.setText(film.getDate());
        mDetailBinding.primaryMovieInfo.tvOverview.setText(film.getOverview());

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
        mReviewsRecyclerView.setHasFixedSize(false);
        mReviewAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        MOVIE_ID = film.getId();


        LoaderManager.LoaderCallbacks<Film> callback = DetailActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, bundleForLoader, callback);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, bundleForLoader, callback);

        mDetailBinding.primaryMovieInfo.buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = film.getTitle();
                String date = film.getDate();
                String posterPath = film.getPosterPath();

                String overview = film.getOverview();
                int filmId = film.getId();
                double vote = film.getVote();

                ContentValues contentValues = new ContentValues();

                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, date);
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, filmId);
                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, posterPath);
                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE, vote);

                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                if (uri != null) {
                    Toast.makeText(getBaseContext(), title + " has been added to your Favorite", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }


    @Override
    public Loader<Film> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case TRAILERS_LOADER_ID: {
                return new AsyncTaskLoader<Film>(this) {

                    Film film = null;

                    @Override
                    protected void onStartLoading() {

                        mDetailBinding.tvNoTrailer.setVisibility(GONE);

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

                        mDetailBinding.tvNoReview.setVisibility(GONE);

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
    public void onLoadFinished(Loader<Film> loader, Film film) {

        if (film.getAuthor() != null) {
            mReviewAdapter.setReviewData(film);
        }

        if (film.getTrailerId() != null) {
            mTrailerAdapter.setTrailerData(film);
        }

        }


    @Override
    public void onLoaderReset(Loader<Film> loader) {

    }

    @Override
    public void onClick(String youtubeId) {

        Log.d(TAG, "YouTube id: " + youtubeId);
        Intent watchIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + youtubeId);
        watchIntent.setData(uri);
        String title = "Choose where to play the video";

        Intent chooser = Intent.createChooser(watchIntent, title);


        if (watchIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}