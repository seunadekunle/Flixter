package com.example.flixter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    private static String API_KEY;
    private static String NOW_PLAYING_URL;
    public static final String TAG = String.valueOf(MainActivity.class);

    List<Movie> movies;
    RecyclerView rvMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // change text and color
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00000'>Flix</font>"));
        getSupportActionBar().setElevation(0);

        API_KEY = getString(R.string.movieDB);
        NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s&language=en-US&page=1", API_KEY);

        rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // 1. Attach adapter to RecyclerView
        rvMovies.setAdapter(movieAdapter);

        // 2. Use LayoutManager on RecyclerView
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // If HTTP status is OK
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    movies.addAll(Movie.fromJSONArray(results));
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                // If HTTP status is error
                Log.d(TAG, "onFailure");
            }
        });
    }
}