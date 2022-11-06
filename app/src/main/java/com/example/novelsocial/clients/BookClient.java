package com.example.novelsocial.clients;


import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class BookClient {

    private static final String API_BASE_URL = "https://openlibrary.org/api/books?";
    private final AsyncHttpClient client;

    public BookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void getBook(final String bookId, JsonHttpResponseHandler handler) {
        String url = getApiUrl("bibkeys=OLID:" + bookId + "&jscmd=data&format=json");
        client.get(url, handler);
    }
}
