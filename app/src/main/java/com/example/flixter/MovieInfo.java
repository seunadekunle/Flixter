package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.models.Movie;
import com.example.flixter.utils.DeviceDimensionsHelper;

import org.parceler.Parcels;

import java.util.function.ToIntFunction;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieInfo extends AppCompatActivity {

    Movie movie;

    ImageView mvPoster;
    ImageView mvIcon;
    TextView mvTitle;
    TextView mvDetail;
    TextView rtDetail;
    RatingBar rbRating;
    View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        // unwrap movie passed through intent using Movie name
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieInfo", "Movie name " + movie.getTitle());

        // hide action bar
        getSupportActionBar().hide();

        // fills in layout with information
        mvPoster = findViewById(R.id.mvPoster);
        mvIcon = findViewById(R.id.mvIcon);
        overlay = findViewById(R.id.overlay);

        // get the width and height of the device app is running on
        int heightPX = DeviceDimensionsHelper.getDisplayHeight(this);
        int widthPX = DeviceDimensionsHelper.getDisplayWidth(this);

        int heightPos = 0, widthPos = 0;
        int heightIcon = 0, widthIcon = 0;

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            heightPos = (int) Math.round(heightPX / 1.5);
            widthPos = (int) Math.round(widthPX / 2.0);
        } else {
            heightPos = (int) Math.round(heightPX / 2.0);
            widthPos = (int) Math.round(widthPX / 1.5);
        }

//        heightIcon = heightPX;
//        widthIcon = widthPX;

        // get the layout params for the imageView
        ViewGroup.LayoutParams params_poster = (ViewGroup.LayoutParams) mvPoster.getLayoutParams();
        params_poster.height = heightPos;
        params_poster.width = widthPos;

        // get the layout params for the overlay
        ViewGroup.LayoutParams params_overlay = (ViewGroup.LayoutParams) overlay.getLayoutParams();
        params_overlay.height = heightPos - 5;
        params_overlay.width = widthPos - 5;

        mvTitle = findViewById(R.id.mvTitle);
        mvDetail = findViewById(R.id.mvDetail);
        rbRating = findViewById(R.id.rbRating);
        rtDetail = findViewById(R.id.rtDetail);

        mvTitle.setText(movie.getTitle());
        mvDetail.setText(movie.getOverview());

        float rating = movie.getScore().floatValue() / 2.0f;
        rbRating.setRating(rating);
        rtDetail.setText(rating + " / 5");

        // handles code in case of orientation changing for image thumbnail
        String imgUrl;
        int placeHolder;

        // if phone is in landscape use backdrop
        if (this.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imgUrl = movie.getBackdropPath();
            placeHolder = R.drawable.flicks_backdrop_placeholder;
        } else {  // else use poster image
            imgUrl = movie.getPosterPath();
            placeHolder = R.drawable.flicks_movie_placeholder;
        }

        // Load images using Glide library
        Glide.with(this)
                .load(imgUrl)
                .placeholder(placeHolder)
                .centerCrop()
                .transform(new RoundedCornersTransformation(MovieAdapter.radius + 20, MovieAdapter.margin))
                .into(mvPoster);
        Glide.with(this)
                .load(R.drawable.yt_icon)
                .into(mvIcon);
    }

    public void goToTrailer(View view) {

        Integer id = movie.getId();

        // if id isn't null
        if (id != null) {
            // creates new intent
            Intent i = new Intent(this, MovieTrailerActivity.class);

            // passes in the id as a value
            i.putExtra("id", id);

            // show new Activity
            startActivity(i);
        }

    }
}