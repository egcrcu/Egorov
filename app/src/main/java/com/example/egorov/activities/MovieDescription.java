package com.example.egorov.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.egorov.R;
import com.squareup.picasso.Picasso;

public class MovieDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);

        Intent intent = getIntent();
        String movieName = intent.getStringExtra("movieName");
        String moviePoster = intent.getStringExtra("moviePoster");
        String movieDescription = intent.getStringExtra("movieDescription");
        String movieGenre = intent.getStringExtra("movieGenre");
        String movieCountry = intent.getStringExtra("movieCountry");

        ImageView imageView = findViewById(R.id.posterImageView);
        Picasso.get().load(moviePoster).into(imageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(movieName);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(movieDescription);
        TextView genreTextView = findViewById(R.id.genreTextView);
        genreTextView.setText("Жанр: " + movieGenre);
        TextView countryTextView = findViewById(R.id.countryTextView);
        countryTextView.setText("Страна: " + movieCountry);
    }

}