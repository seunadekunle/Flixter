package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    private String API_KEY_MOVIE, API_KEY_TRAILER, TRAILER_URL;
    public static final String TAG = String.valueOf(MovieTrailerActivity.class);

    String videoId;
    String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // get id value passed from intent
        movieId = String.valueOf(getIntent().getIntExtra("id", 000000));
        // temporary test video id -- TODO replace with movie trailer video id
        videoId = "tKodtNFpzBA";

        // retrieve api key from values file
        API_KEY_TRAILER = getString(R.string.google);
        API_KEY_MOVIE = getString(R.string.movieDB);
        TRAILER_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movieId, API_KEY_MOVIE);

        // resolve the player view
        YouTubePlayerView playerView = findViewById(R.id.ytPlayer);

        // api to get trailer array
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TRAILER_URL, new JsonHttpResponseHandler() {

            // if api call is successful play video
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray result = jsonObject.getJSONArray("results");

                    Integer index= 0;
                    for (int ind = 0; ind < result.length(); ind++) {

                        JSONObject currObj = result.getJSONObject(ind);
                        if (currObj.getString("site") == "YouTube" && currObj.getString("type") == "Trailer") {
                            index = ind;
                        }
                    }

                    videoId = result.getJSONObject(index).getString("key");

                    // initialize player view after data has been collected
                    playerView.initialize(API_KEY_TRAILER, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            // autoplay video
                            youTubePlayer.loadVideo(videoId);

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e(TAG, "Error initializing youtube player");
                        }
                    });
                } catch (JSONException e) {
                    // if calls fails
                    e.printStackTrace();
                }
            }

            // if api call fails
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG, "JSON Exception", throwable);
            }
        });
    }
}