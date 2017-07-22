package com.example.henrikhoang.projectmoviestage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.henrikhoang.projectmoviestage1.Settings.SettingsActivity;
import com.example.henrikhoang.projectmoviestage1.adapter.MovieAdapter;
import com.example.henrikhoang.projectmoviestage1.data.MoviePreferences;
import com.example.henrikhoang.projectmoviestage1.utility.Network;
import com.example.henrikhoang.projectmoviestage1.utility.OpenMovieJsonUtils;

import org.parceler.Parcels;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Film>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = MainActivity.class.getSimpleName();


    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private static final int MOVIE_LOADER_ID = 17;

    private Context mContext;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movieCatalogue);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        int loaderId = MOVIE_LOADER_ID;

        LoaderManager.LoaderCallbacks<List<Film>> callbacks = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callbacks);
        Log.d(TAG, "onCreate: registering preference changed listener");
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public Loader<List<Film>> onCreateLoader(int loaderId, Bundle args) {


        return new AsyncTaskLoader<List<Film>>(this) {
            List<Film> films = null;

            @Override
            protected void onStartLoading() {
               if (films != null) {
                   deliverResult(films);
               } else {
                   mLoadingIndicator.setVisibility(View.VISIBLE);
                   forceLoad();
               }
            }

            @Override
            public List<Film> loadInBackground() {

                try {
                    String sortBy = MoviePreferences.getPreferredSort(MainActivity.this);
                    Log.d(TAG, "Sort by " + sortBy);
                    if (sortBy.equals("popular")) {

                    URL movieRequestURL = Network.buildURL(MainActivity.this, sortBy);

                    String jsonMovieResponse = Network.
                            getResponseFromHttpUrl(movieRequestURL);

                    List<Film> movies = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this,
                            jsonMovieResponse);
                    return movies;
                } else {
                        URL movieRequestURL = Network.buildURL(MainActivity.this, "top_rated");

                        String jsonMovieResponse = Network.
                                getResponseFromHttpUrl(movieRequestURL);

                        List<Film> movies = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(MainActivity.this,
                                jsonMovieResponse);
                        return movies;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Film> data) {
                films = data;
                super.deliverResult(data);
            }
        };



    }


    @Override
    public void onClick(Film film) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClass(context, destinationClass);
        intent.putExtra("film", Parcels.wrap(film));
        startActivity(intent);

    }

    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setWeatherData(data);
        if (null == data) {
            return;
        } else { showMovieDataView(); }
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {

    }

    private void invalidateData() { mMovieAdapter.setWeatherData(null);}

    private void showMovieDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
                invalidateData();
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;

            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}



