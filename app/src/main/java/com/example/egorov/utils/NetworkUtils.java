package com.example.egorov.utils;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String basedUrl = "https://kinopoiskapiunofficial.tech";
    private final static String popularFilms = "/api/v2.2/films/top";
    private final static String film = "/api/v2.2/films/";
    private final static String popularFilmsType = "TOP_100_POPULAR_FILMS";
    private final static String apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b";

    public URL generateURL() throws MalformedURLException {
        Uri buildUri = Uri.parse(basedUrl + popularFilms)
                .buildUpon()
                .appendQueryParameter("type", popularFilmsType)
                .build();
        URL url;
        url = new URL(buildUri.toString());
        return url;
    }

    public URL generateDescription(String id) throws MalformedURLException {
        Uri buildUri = Uri.parse(basedUrl + film + id)
                .buildUpon()
                .build();
        URL url;
        url = new URL(buildUri.toString());
        return url;
    }

    public String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("x-api-key", apiKey);
            in = urlConnection.getInputStream();
            if (in != null) {
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                if (scanner.hasNext()) {
                    return scanner.next();
                }
            }
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
    }

}
