package com.example.henrikhoang.projectmoviestage1.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.henrikhoang.projectmoviestage1.FavoriteActivity;
import com.example.henrikhoang.projectmoviestage1.Film;
import com.example.henrikhoang.projectmoviestage1.R;
import com.squareup.picasso.Picasso;

/**
 * Created by henrikhoang on 8/4/17.
 */

public class FavoriteMovieAdapter extends
        RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieAdapterViewHolder> {

    public static final String TAG = FavoriteActivity.class.getSimpleName();
    private final Context mContext;
    final private FavoriteMovieAdapterOnClickHandler mClickHandler;

    public interface FavoriteMovieAdapterOnClickHandler {
        void onClick(Film film);
    }

    private Cursor mCursor;

    public FavoriteMovieAdapter(@NonNull Context context, FavoriteMovieAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public FavoriteMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.fav_movie_recyclerview, parent, false);

        view.setFocusable(true);
        return new FavoriteMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        String posterPath = mCursor.getString(FavoriteActivity.INDEX_MOVIE_POSTER);

        Log.d(TAG, "DATABASE POSTER PATH: " + posterPath);

        int id = mCursor.getInt(FavoriteActivity.INDEX_MOVIE_ID);
        String title = mCursor.getString(FavoriteActivity.INDEX_MOVIE_TITLE);

        holder.movieTitle.setText(title);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342" + posterPath)
                .into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    class FavoriteMovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView movieTitle;
        ImageView moviePoster;

        FavoriteMovieAdapterViewHolder(View view) {
            super(view);
            movieTitle = (TextView) view.findViewById(R.id.fav_movie_name_tv);
            moviePoster = (ImageView) view.findViewById(R.id.fav_movie_poster_image_view);

            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            Film film = new Film();

            film.setId(mCursor.getInt(FavoriteActivity.INDEX_MOVIE_ID));
            film.setOverview(mCursor.getString(FavoriteActivity.INDEX_MOVIE_OVERVIEW));
            film.setVote(mCursor.getDouble(FavoriteActivity.INDEX_MOVIE_VOTE));
            film.setDate(mCursor.getString(FavoriteActivity.INDEX_MVOIE_RELEASE_DATE));
            film.setTitle(mCursor.getString(FavoriteActivity.INDEX_MOVIE_TITLE));
            film.setPosterPath(mCursor.getString(FavoriteActivity.INDEX_MOVIE_POSTER));
            mClickHandler.onClick(film);
        }
    }
}
