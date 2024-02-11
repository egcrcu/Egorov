package com.example.egorov.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.egorov.R;
import com.example.egorov.model.Movie;
import com.example.egorov.utils.MovieAdapter;
import com.example.egorov.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private final NetworkUtils networkUtils = new NetworkUtils();
    private AlertDialog tryAgainDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        Objects.requireNonNull(getSupportActionBar()).setTitle("Популярные");
        SpannableString spannableString = new SpannableString("Популярные");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(spannableString);

        RecyclerView movieRecyclerView = findViewById(R.id.movieRecyclerView);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(MainActivity.this, movieList);
        movieRecyclerView.setAdapter(movieAdapter);

        executorService.execute(() -> {
            try {
                URL url = networkUtils.generateURL();
                String response = networkUtils.getResponse(url);
                if (response != null) {
                    movieList = parseMovieData(response);
                    runOnUiThread(() -> movieAdapter.setMovieList(movieList));
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                } else {
                    runOnUiThread(this::showTryAgainDialog);
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(this::showTryAgainDialog);
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        });

    }

    private ArrayList<Movie> parseMovieData(String response) throws JSONException, IOException {
        ArrayList<Movie> movieList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("films")) {
            JSONArray jsonArray = jsonObject.getJSONArray("films");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieObject = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(movieObject.getString("filmId"));
                String name = movieObject.getString("nameRu");
                String year = movieObject.getString("year");
                String genre = movieObject.getJSONArray("genres").getJSONObject(0).getString("genre");
                String imageUrl = movieObject.getString("posterUrlPreview");
                String posterUrl = movieObject.getString("posterUrl");
                String country = movieObject.getJSONArray("countries").getJSONObject(0).getString("country");

                URL url = networkUtils.generateDescription(String.valueOf(id));
                String responseDesc = networkUtils.getResponse(url);
                JSONObject jsonObjectDesc = new JSONObject(responseDesc);
                String description = jsonObjectDesc.getString("description");

                movieList.add(new Movie(id, name, year, genre, imageUrl, posterUrl, country, description));
            }
        }
        return movieList;
    }

    private void showTryAgainDialog() {
        if (tryAgainDialog == null || !tryAgainDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
            builder.setView(dialogView);
            tryAgainDialog = builder.create();

            TextView errorTextView = dialogView.findViewById(R.id.error_message);
            errorTextView.setText("Произошла ошибка при загрузке данных, проверьте подключение к сети.");

            Button tryAgainButton = dialogView.findViewById(R.id.button_try_again);
            tryAgainButton.setOnClickListener(v -> {
                tryAgainDialog.dismiss();
                retryLoadingData();
            });

            tryAgainDialog.show();
        }
    }

    private void retryLoadingData() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
        executorService.execute(() -> {
            try {
                URL url = networkUtils.generateURL();
                String response = networkUtils.getResponse(url);
                if (response != null) {
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                    movieList = parseMovieData(response);
                    runOnUiThread(() -> movieAdapter.setMovieList(movieList));
                } else {
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                    runOnUiThread(this::showTryAgainDialog);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                runOnUiThread(this::showTryAgainDialog);
            }
        });
    }

}

