package com.example.henrikhoang.projectmoviestage1.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.henrikhoang.projectmoviestage1.Film;
import com.example.henrikhoang.projectmoviestage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by henrikhoang on 5/25/17.
 */

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;
    private List<Film> mFilms;
    private final MovieAdapterOnClickHandler mClickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick (Film selectedMovieId);


    }

    private Cursor mCursor;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler,@NonNull Context ctx) {
        mClickHandler = clickHandler;
        mContext = ctx;
    }



    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)  {

       Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        final Film film = mFilms.get(position);
        String movieBeingSelectedPoster = film.getPosterPath();
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500" + movieBeingSelectedPoster)
                .into(holder.mMovieImageView);


    }

    @Override
    public int getItemCount() {
        if (null == mFilms) return 0;
        return mFilms.size();

    }

    public void setMovieData(List<Film> films) {
        mFilms = films;
        notifyDataSetChanged();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {


        final ImageView mMovieImageView;

         MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.tv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Film film = mFilms.get(adapterPosition);

            mClickHandler.onClick(film);
        }
    }
}
