package com.example.henrikhoang.projectmoviestage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.henrikhoang.projectmoviestage1.Film;
import com.example.henrikhoang.projectmoviestage1.R;

/**
 * Created by henrikhoang on 7/23/17.
 */

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final Context mContext;
    private Film mFilm;

    public interface ReviewAdapterOnClickHandler {
    }

    public ReviewAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListReviews = R.layout.reviews_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListReviews, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        String author = mFilm.getAuthor()[position];
        String comment = mFilm.getComment()[position];

        holder.mAuthorTextView.setText(author);
        holder.mCommentTextView.setText(comment);
    }

    public void setReviewData(Film film) {
        mFilm = film;
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder  {
        final TextView mAuthorTextView;
        final TextView mCommentTextView;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
            mCommentTextView = (TextView) view.findViewById(R.id.tv_review_comment);
        }


    }

    @Override
    public int getItemCount() {
        if (0 == mFilm.getAuthor().length) return 0;
        return mFilm.getAuthor().length;
    }
}

