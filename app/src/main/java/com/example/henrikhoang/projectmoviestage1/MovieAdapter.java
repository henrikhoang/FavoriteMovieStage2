package com.example.henrikhoang.projectmoviestage1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by henrikhoang on 5/25/17.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;
//    private List<Film> films;
    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick (String movieSelected);
    }

    private Cursor mCursor;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, Context ctx) {
        mClickHandler = clickHandler;
        mContext = ctx;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {


        private final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.tv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           String movieSeleceted = mMovieImageView.getI
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)  {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentsImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentsImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
//        final Film film = films.get(position);
//        String movieBeingSelectedPoster = film.getPosterPath();
//        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500" + movieBeingSelectedPoster).into(holder.mMovieImageView);

        mCursor.moveToPosition(position);

        String movieBeingSelectedPoster = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER);

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500" + movieBeingSelectedPoster).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
//        if (null == films) return 0;
//        return films.size();

        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
    public void setMovieData(List<Film> movies) {
        films = movies;
        notifyDataSetChanged();
    }
}
