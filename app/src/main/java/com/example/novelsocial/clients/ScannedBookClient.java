package com.example.novelsocial.clients;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class ScannedBookClient {
    public static final String API_BASE_URL = "https://openlibrary.org/";
    public AsyncHttpClient client;

    public ScannedBookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void getBooks(final String isbn, JsonHttpResponseHandler handler) {
            String url = getApiUrl("api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json");
            client.get(url, handler);
    }
}
