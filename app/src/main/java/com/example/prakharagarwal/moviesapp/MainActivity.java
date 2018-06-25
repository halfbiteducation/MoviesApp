package com.example.prakharagarwal.moviesapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
                movieList.add(movie);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
