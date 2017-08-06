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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.henrikhoang.projectmoviestage1.adapter.FavoriteMovieAdapter;
import com.example.henrikhoang.projectmoviestage1.data.MovieContract;

import org.parceler.Parcels;

public class FavoriteActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FavoriteMovieAdapter.FavoriteMovieAdapterOnClickHandler {

    public static final String TAG = FavoriteActivity.class.getSimpleName();

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry._ID,
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_POSTER = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;
    public static final int INDEX_MOVIE_VOTE = 4;
    public static final int INDEX_MOVIE_OVERVIEW = 5;
    public static final int INDEX_ID = 6;


    private static final int ID_FAV_MOVIE_LOADER = 18;

    private int mPosition = RecyclerView.NO_POSITION;

    private RecyclerView mRecyclerView;
    private FavoriteMovieAdapter mFavMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_favorite);
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFavMovieAdapter = new FavoriteMovieAdapter(this, this);
        mRecyclerView.setAdapter(mFavMovieAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                getContentResolver().delete(uri, null, null);

                getSupportLoaderManager().restartLoader(ID_FAV_MOVIE_LOADER, null, FavoriteActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);
        getSupportLoaderManager().initLoader(ID_FAV_MOVIE_LOADER, null, this);

    }



    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(ID_FAV_MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FAV_MOVIE_LOADER:
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                String sortOrder = MovieContract.MovieEntry._ID + " ASC";
                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavMovieAdapter.swapCursor(null);
    }

    @Override
    public void onClick(Film film) {
        Intent intent = new Intent(this, FavMovieDetailActivity.class);
        intent.putExtra("film", Parcels.wrap(film));
        startActivity(intent);
    }
}
