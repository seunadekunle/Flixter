package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixter.MovieInfo;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public static int radius = 25; // corner radius
    public static int margin = 5;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflate Layout and return in ViewHolder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        Log.d("MovieAdapter", "onCreateViewHolder");
        // Inflate layout for each item
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onCreateViewHolder " + position);

        // Get movie at the passed in position
        Movie movie = movies.get(position);

        // Bind movie data to UI element
        holder.bind(movie);
    }

    // Return total count of item in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Handles each view
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
//        RecyclerView movieCard;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
//            movieCard = itemView.findViewById(R.id.rvMovies);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imgUrl;
            int placeHolder;
            // if phone is in landscape use backdrop
            if (context.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imgUrl = movie.getBackdropPath();
                placeHolder = R.drawable.flicks_backdrop_placeholder;
            }
            else {  // else use poster image
                imgUrl = movie.getPosterPath();
                placeHolder = R.drawable.flicks_movie_placeholder;
            }

            // Load image using Glide library
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(placeHolder)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();

            // if position isn't out of bounds
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);

                // intent for new activity
                Intent i = new Intent(context, MovieInfo.class);
                // passed serialized object
                i.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                // show new Activity
                context.startActivity(i);
            }
        }
    }
}
