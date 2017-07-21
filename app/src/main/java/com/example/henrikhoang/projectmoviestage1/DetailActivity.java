package com.example.henrikhoang.projectmoviestage1;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Film> {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private TextView mDisplayInfo;
    private ImageView mDisplayPoster;
    private TextView mDisplayOverview;
    private VideoView mDisplayTrailer;
    public static int BOOM = 0;
    private static final int REVIEW_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDisplayInfo = (TextView) findViewById(R.id.tv_movie_info);
        mDisplayPoster = (ImageView) findViewById(R.id.iv_movie_thumbnail);
//        mDisplayOverview = (TextView) findViewById(R.id.tv_display_movie_overview);
        mDisplayTrailer = (VideoView) findViewById(R.id.movie_trailer);
        mDisplayInfo.setMovementMethod(new ScrollingMovementMethod());


        Film film = Parcels.unwrap(getIntent().getParcelableExtra("film"));
        mDisplayInfo.setText("TITLE: " + "\n" + film.getTitle() + "\n\n" + "RATING: " + film.getVote()+
                "/10" + "\n\n" + "RELEASE DATE: " + film.getDate());
        mDisplayOverview.setText(film.getOverview());
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500"+film.getPosterPath()).into(mDisplayPoster);
//        mDisplayTrailer.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=ByehYal_cCs"));

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
                    String jsonMovieReposnse = Network.getResponseFromHttpUrl(reviewRequestURL);
                    Film film = OpenMovieJsonUtils.getTrailersUrlFromJson(DetailActivity.this, jsonMovieReposnse);
                    return film;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Film data) {
                film = data;
                super.deliverResult(data);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Film> loader, Film data) {
        mDisplayOverview.setText(data.getAuthor()[0]);
        if (null == data) {
            return;
        }

    }

    @Override
    public void onLoaderReset(Loader<Film> loader) {

    }
}