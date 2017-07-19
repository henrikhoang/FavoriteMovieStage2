package com.example.henrikhoang.projectmoviestage1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.henrikhoang.projectmoviestage1.Settings.SettingsActivity;
import com.example.henrikhoang.projectmoviestage1.data.MovieContract;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = MainActivity.class.getSimpleName();


    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER
    };


    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_POSTER = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private static final int MOVIE_LOADER_ID = 17;

    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movieCatalogue);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = MOVIE_LOADER_ID;
        showLoading();
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
//        MovieSyncUtils.initialize(this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {
            case MOVIE_LOADER_ID:
                Uri forecastQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented" + loaderId);

        }
    }
//        return new AsyncTaskLoader<List<Film>>(this) {
//            List<Film> films = null;
//
//            @Override
//            protected void onStartLoading() {
//               if (films != null) {
//                   deliverResult(films);
//               } else {
//                   mLoadingIndicator.setVisibility(View.VISIBLE);
//                   forceLoad();
//               }
//            }
//
//            @Override
//            public List<Film> loadInBackground() {
//
//                try {
//                    String sortBy = MoviePreferences.getPreferredSort(MainActivity.this);
//                    Log.d(TAG, "Sort by " + sortBy);
//                    if (sortBy.equals("popular")) {
//
//                    URL movieRequestURL = Network.buildURL(MainActivity.this, sortBy);
//
//                    String jsonMovieResponse = Network.
//                            getResponseFromHttpUrl(movieRequestURL);
//
//                    List<Film> movies = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this,
//                            jsonMovieResponse);
//                    return movies;
//                } else {
//                        URL movieRequestURL = Network.buildURL(MainActivity.this, "top_rated");
//
//                        String jsonMovieResponse = Network.
//                                getResponseFromHttpUrl(movieRequestURL);
//
//                        List<Film> movies = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this,
//                                jsonMovieResponse);
//                        return movies;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            public void deliverResult(List<Film> data) {
//                films = data;
//                super.deliverResult(data);
//            }
//        };


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    @Override
    public void onClick(int movieId) {

        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.setData(MovieContract.MovieEntry.buildMovieUriWithId(movieId));
        startActivity(movieDetailIntent);

    }

    private void showMovieDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //activities for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



