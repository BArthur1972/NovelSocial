package com.example.novelsocial.client;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookSubjectClient {
    private static final String API_BASE_URL = "https://openlibrary.org/";
    private final AsyncHttpClient client;

    public BookSubjectClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void getBooks(final String genre, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("/subjects/" + genre.toLowerCase() + ".json?limit=50");
            client.get(url + URLEncoder.encode(genre, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}