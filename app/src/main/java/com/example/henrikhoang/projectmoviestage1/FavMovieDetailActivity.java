package com.example.henrikhoang.projectmoviestage1;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.henrikhoang.projectmoviestage1.databinding.ActivityFavMovieDetailBinding;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import static android.view.View.GONE;

public class FavMovieDetailActivity extends AppCompatActivity {

    private ActivityFavMovieDetailBinding mFavMovDetailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavMovDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_fav_movie_detail);


        final Film film = Parcels.unwrap(getIntent().getParcelableExtra("film"));
        mFavMovDetailBinding.favMovieDetailLayout.tvMovieTitle.setText(film.getTitle());
        mFavMovDetailBinding.favMovieDetailLayout.tvReleaseDate.setText(film.getDate());
        mFavMovDetailBinding.favMovieDetailLayout.tvRating.setText(String.valueOf(film.getVote()) + "/10");
        mFavMovDetailBinding.favMovieDetailLayout.tvOverview.setText(film.getOverview());

        Picasso.with(this).load("http://image.tmdb.org/t/p/w500" + film.getPosterPath())
                .into(mFavMovDetailBinding.favMovieDetailLayout.ivMoviePoster);
        mFavMovDetailBinding.favMovieDetailLayout.buttonFav.setVisibility(GONE);

    }

}
