package com.example.prakharagarwal.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prakharagarwal on 25/06/18.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    MovieViewHolder holder;
    List<Movie> moviesList;
    Context context;

    public MovieRecyclerAdapter(Context context,  List<Movie> moviesList){
        this.context=context;
        this.moviesList=moviesList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_list, parent, false);
        holder = new MovieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movie movie=moviesList.get(position);
        String posterUrl="https://image.tmdb.org/t/p/original"+movie.getPoster_path();
        Picasso.with(context).load(posterUrl).into(holder.poster);
        holder.title.setText(movie.getTitle());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        TextView title;
        public MovieViewHolder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.image_poster);
            title= itemView.findViewById(R.id.title);
        }
    }
}
