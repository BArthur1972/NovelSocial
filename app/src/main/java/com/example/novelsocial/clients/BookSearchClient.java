package com.example.novelsocial.clients;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BookSearchClient {
    private static final String API_BASE_URL = "https://openlibrary.org/";
    private final AsyncHttpClient client;

    public BookSearchClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl() {
        return API_BASE_URL + "search.json?q=";
    }

    public void getBooks(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl();
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
