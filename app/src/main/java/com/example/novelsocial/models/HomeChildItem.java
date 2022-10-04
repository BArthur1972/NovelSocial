package com.example.novelsocial.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeChildItem {

    private String bookTitle;
    private String openLibraryId;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public void setOpenLibraryId(String openLibraryId) {
        this.openLibraryId = openLibraryId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookCoverUrl() {
        return "https://covers.openlibrary.org/b/olid/"+ openLibraryId +"-L.jpg?default=false";
    }

    public HomeChildItem() {
    }

    // Parse a JSONObject for a book's details and return a Book object
    public static HomeChildItem childItemFromJson(JSONObject jsonObject) {
        HomeChildItem book = new HomeChildItem();
        try {
            // Assign the OpenLibraryId for the book
            if (jsonObject.has("cover_edition_key")) {
                book.setOpenLibraryId(jsonObject.getString("cover_edition_key"));
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.setOpenLibraryId(ids.getString(0));
            }

            // Assign title and author
            book.setBookTitle(jsonObject.has("title") ? jsonObject.getString("title") : "");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    // Get a list of Books from a JSON object
    public static ArrayList<HomeChildItem> childItemFromJson(JSONArray jsonArray) {
        ArrayList<HomeChildItem> books = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            HomeChildItem book = childItemFromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }
}
