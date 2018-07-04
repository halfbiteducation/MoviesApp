package com.example.prakharagarwal.moviesapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    List<Movie> movieList=new ArrayList<>();
    List<Movie> bollywoodMovieList=new ArrayList<>();
    MovieRecyclerAdapter adapter;
    MovieRecyclerAdapter bollywoodAdapter;
    TextView showHide;
    boolean showHideFlag=false;
    ImageView arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        adapter=new MovieRecyclerAdapter(this,movieList);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);

        bollywoodAdapter=new MovieRecyclerAdapter(this,bollywoodMovieList);
        recyclerView2=findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setAdapter(bollywoodAdapter);
        getMovieData();
        getBollywoodData();
        showHide=findViewById(R.id.bollywood_text_show);
        arrow=findViewById(R.id.image_arrow);
        recyclerView2.setVisibility(View.GONE);
        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showHideFlag)
                    fadeHide();
                else

                fadeShow();
            }
        });
    }

    private void fadeHide() {

        recyclerView2.animate().alpha(0f)
                .setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recyclerView2.setVisibility(View.GONE);
                    }
                });
        showHide.setText("Show");
        showHideFlag=false;

    }

    private void fadeShow() {
        recyclerView2.setAlpha(0f);
        recyclerView2.setVisibility(View.VISIBLE);

        recyclerView2.animate().alpha(1f)
                .setDuration(2000)
                .setListener(null);
        showHide.setText("Hide");
        showHideFlag=true;
//        AnimationDrawable drawable= (AnimationDrawable) arrow.getBackground();
//        drawable.start();
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        arrow.startAnimation(startRotateAnimation);
        startRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setRotation(180);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getBollywoodData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", "9d7f4e0380328f76105348f51ef53964")
                .appendQueryParameter("region", "IN")
                .appendQueryParameter("sort_by", "vote_average.desc");
        String myUrl = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseBollywoodData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    private void parseBollywoodData(String response) {
        try {
            JSONObject resultJson=new JSONObject(response);
            JSONArray resultsArray= resultJson.getJSONArray("results");
            for(int i=0;i<resultsArray.length();i++){
                JSONObject movieJson=resultsArray.optJSONObject(i);
                Movie movie= new Movie();
                movie.setId(movieJson.getInt("id"));
                movie.setPoster_path(movieJson.getString("poster_path"));
                movie.setTitle(movieJson.getString("title"));
                movie.setOverview(movieJson.getString("overview"));
                bollywoodMovieList.add(movie);
            }
            bollywoodAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getMovieData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=9d7f4e0380328f76105348f51ef53964" +
                "&language=en-US&sort_by=popularity.desc";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", "9d7f4e0380328f76105348f51ef53964")
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("sort_by", "popularity.desc");
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
        try {
            JSONObject resultJson=new JSONObject(response);
            JSONArray resultsArray= resultJson.getJSONArray("results");
            for(int i=0;i<resultsArray.length();i++){
                JSONObject movieJson=resultsArray.optJSONObject(i);
                Movie movie= new Movie();
                movie.setId(movieJson.getInt("id"));
                movie.setPoster_path(movieJson.getString("poster_path"));
                movie.setTitle(movieJson.getString("title"));
                movie.setOverview(movieJson.getString("overview"));
                movieList.add(movie);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
