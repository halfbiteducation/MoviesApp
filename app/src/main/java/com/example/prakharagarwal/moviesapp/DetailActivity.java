package com.example.prakharagarwal.moviesapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView title;
    TextView overview;
    private YouTubePlayerView youTubeView;
    String youtubekey = null;
    YouTubePlayer player;
    //    List<String> reviews=new ArrayList<>();
    List<Review> reviews = new ArrayList<>();
    LinearLayout reviewsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        title = findViewById(R.id.title);
        overview = findViewById(R.id.overview);
        reviewsContainer = findViewById(R.id.reviews_container);

        if (getIntent().getStringExtra("title") != null) {
            title.setText(getIntent().getStringExtra("title"));
            overview.setText(getIntent().getStringExtra("overview"));
            getMovieVideos(getIntent().getIntExtra("movieId", 0));
            getReviews(getIntent().getIntExtra("movieId", 0));
        }
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        youTubeView.initialize(getString(R.string.youtubeKey), this);

    }

    private void getReviews(int movieId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/movie/351286/reviews?api_key=9d7f4e0380328f76105348f51ef53964&language=en-US&page=1";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId + "")
                .appendPath("reviews")
                .appendQueryParameter("api_key", "9d7f4e0380328f76105348f51ef53964");
        String myUrl = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseDataReviews(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    private void parseDataReviews(String response) {
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(response);
            JSONArray resultsArray = resultJson.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject review = (JSONObject) resultsArray.get(i);
                String reviewString = review.getString("content");
                String reviewName = review.getString("author");
//                reviews.add(reviewString);
                Review review1 = new Review();
                review1.setAuthor(reviewName);
                review1.setContent(reviewString);
                reviews.add(review1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateReviewUI();
    }

    private void updateReviewUI() {
//        TextView textView=new TextView(this);
//        textView.setText(reviews.get(0));
//        reviewsContainer.addView(textView);

        for (int i = 0; i < reviews.size(); i++) {
            Review review=reviews.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.review_item, null);
            TextView reviewName = view.findViewById(R.id.review_name);
            TextView review_content= view.findViewById(R.id.review_content);
            reviewName.setText(review.getAuthor());
            review_content.setText(review.getContent());
            reviewsContainer.addView(view);

        }
    }

    private void getMovieVideos(int movieId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=9d7f4e0380328f76105348f51ef53964";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId + "")
                .appendPath("videos")
                .appendQueryParameter("api_key", "9d7f4e0380328f76105348f51ef53964");
        String myUrl = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    private void parseData(String response) {
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(response);
            JSONArray resultsArray = resultJson.getJSONArray("results");
            JSONObject object = (JSONObject) resultsArray.get(0);
            youtubekey = object.getString("key");
            if (player != null) {
                player.cueVideo(youtubekey);
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        // youTubePlayer.cueVideo(youtubekey);

        player = youTubePlayer;
        if (youtubekey != null)
            player.cueVideo(youtubekey);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
