package com.example.novelsocial.models;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Book {

    private String openLibraryId;
    private String author;
    private String title;
    private String publisher;
    private int pages;
    private String ISBN;
    private String genre;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookCoverUrl() {
        return "https://covers.openlibrary.org/b/olid/"+ openLibraryId +"-L.jpg?default=false";
    }

    // Parse a JSONObject for a book's details and return a Book object
    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Assign the OpenLibraryId for the book
            if (jsonObject.has("cover_edition_key")) {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }

            // Assign the publisher for the book
            if (jsonObject.has("publisher")) {
                JSONArray jsonArray = jsonObject.getJSONArray("publisher");
                if (jsonArray.length() > 0) {
                    String firstPublisher = (String) jsonArray.get(0);
                    book.setPublisher(firstPublisher);
                }
            }

            // Assign the number of pages for the book
            if (jsonObject.has("number_of_pages_median")) {
                Integer bookPages = (Integer) jsonObject.getInt("number_of_pages_median");
                book.setPages(bookPages);
            }

            // Assign the ISBN code for the book
            if (jsonObject.has("isbn")) {
                JSONArray jsonArray = jsonObject.getJSONArray("isbn");
                String isbn = (String) jsonArray.get(0);
                book.setISBN(isbn);
            }

            if (jsonObject.has("subject")) {
                JSONArray jsonArray = jsonObject.getJSONArray("subject");
                String genre = (String) jsonArray.get(0);
                book.setGenre(genre);
            }

            // Assign title and author
            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    // Get a list of Books from a JSON object
    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }
}
