package com.example.flixter.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Integer id;
    Double score;

    // empty constructor required for parcel
    public Movie() {}

    // constructor normally used
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        score = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
    }

    // method public functions can use
    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getPosterPath() {
        // uses string format with %s
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        // uses string format with %s
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getScore() {
        return score;
    }

    public int getId() { return id; }
}
