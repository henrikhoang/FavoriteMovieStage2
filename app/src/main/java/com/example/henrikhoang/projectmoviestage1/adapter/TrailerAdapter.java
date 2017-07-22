package com.example.henrikhoang.projectmoviestage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.henrikhoang.projectmoviestage1.Film;
import com.example.henrikhoang.projectmoviestage1.R;

/**
 * Created by henrikhoang on 7/21/17.
 */

public class TrailerAdapter extends
        RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final Context mContext;
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Film mFilm;


    public interface TrailerAdapterOnClickHandler {
        void onClick(String youtubeId);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, @NonNull Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListTrailers = R.layout.trailers_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListTrailers, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        int trailerNumber = position + 1;
        holder.mTrailerNumberTextView.setText("Trailer " + String.valueOf(trailerNumber));
    }


    public void setTrailerData(Film film) {
        mFilm = film;
        notifyDataSetChanged();
    }


    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {

        final ImageView mPlayButtonImageView;
        final TextView mTrailerNumberTextView;

        TrailerAdapterViewHolder(View view) {
            super(view);
            mPlayButtonImageView = (ImageView) view.findViewById(R.id.play_button_view);
            mTrailerNumberTextView = (TextView) view.findViewById(R.id.trailer_number_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String youtubeId = mFilm.getTrailerId()[adapterPosition];
            mClickHandler.onClick(youtubeId);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mFilm.getTrailerId()) return 0;
        return mFilm.getTrailerId().length;
    }
}
