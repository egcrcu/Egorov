package com.example.egorov.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.egorov.model.Movie;
import com.example.egorov.activities.MovieDescription;
import com.example.egorov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;
    private final Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieNameTextView.setText(movie.getName());
        holder.yearGenreTextView.setText(movie.getYear() + " | " + movie.getGenre());
        Picasso.get().load(movie.getImageUrl()).into(holder.movieImageView);

        holder.movieContainer.setOnClickListener(v -> {
            Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
            holder.itemView.startAnimation(scaleAnimation);
            Intent intent = new Intent(context, MovieDescription.class);
            intent.putExtra("movieName", movie.getName());
            intent.putExtra("movieDescription", movie.getDescription());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieCountry", movie.getCountry());
            intent.putExtra("moviePoster", movie.getPosterUrl());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImageView;
        TextView movieNameTextView;
        TextView yearGenreTextView;
        ConstraintLayout movieContainer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.movieNameTextView);
            yearGenreTextView = itemView.findViewById(R.id.yearGenreTextView);
            movieImageView = itemView.findViewById(R.id.movieImageView);
            movieContainer = itemView.findViewById(R.id.movieContainer);
        }
    }
}

