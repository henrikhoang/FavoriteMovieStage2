package com.example.henrikhoang.projectmoviestage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private TextView mDisplayInfo;
    private ImageView mDisplayPoster;
    private TextView mDisplayOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDisplayInfo = (TextView) findViewById(R.id.tv_movie_info);
        mDisplayPoster = (ImageView) findViewById(R.id.iv_movie_thumbnail);
        mDisplayOverview = (TextView) findViewById(R.id.tv_display_movie_overview);
        mDisplayInfo.setMovementMethod(new ScrollingMovementMethod());

        Film film = Parcels.unwrap(getIntent().getParcelableExtra("film"));
        mDisplayInfo.setText("TITLE: " + "\n" + film.getTitle() + "\n\n" + "RATING: " + film.getVote()+
                "/10" + "\n\n" + "RELEASE DATE: " + film.getDate());
        mDisplayOverview.setText(film.getOverview());
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500"+film.getPosterPath()).into(mDisplayPoster);

    }
}