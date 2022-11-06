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

    public Book() {
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
                int bookPages = jsonObject.getInt("number_of_pages_median");
                book.setPages(bookPages);
            }

            // Assign the ISBN code for the book
            if (jsonObject.has("isbn")) {
                JSONArray jsonArray = jsonObject.getJSONArray("isbn");
                String isbn = (String) jsonArray.get(0);
                book.setISBN(isbn);
            }

            // Assign the genre of the book
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

    public static Book fromJsonWithScannedISBN(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Assign the OpenLibraryId and ISBN for the book
            if (jsonObject.has("identifiers")) {
                JSONObject identifiers = jsonObject.getJSONObject("identifiers");

                // Assign OpenLibraryId
                if (identifiers.has("openlibrary")) {
                    JSONArray openLibraryArray = identifiers.getJSONArray("openlibrary");
                    if (openLibraryArray.length() > 0) {
                        book.setOpenLibraryId(openLibraryArray.getString(0));
                    }
                }

                // Assign ISBN
                if (identifiers.has("isbn_13")) {
                    JSONArray isbnArray = identifiers.getJSONArray("isbn_13");
                    if (isbnArray.length() > 0) {
                        book.setISBN(isbnArray.getString(0));
                    }
                }
            }

            // Assign the publisher for the book
            if (jsonObject.has("publishers")) {
                JSONArray jsonArray = jsonObject.getJSONArray("publishers");
                if (jsonArray.length() > 0) {
                    JSONObject firstPublisher = (JSONObject) jsonArray.get(0);
                    book.setPublisher(firstPublisher.getString("name"));
                }
            }

            // Assign the number of pages for the book
            if (jsonObject.has("number_of_pages")) {
                int bookPages = jsonObject.getInt("number_of_pages");
                book.setPages(bookPages);
            }

            // Assign the genre of the book
            if (jsonObject.has("subjects")) {
                JSONArray subjectsArray = jsonObject.getJSONArray("subjects");
                JSONObject genreObject = (JSONObject) subjectsArray.get(0);
                String genre = genreObject.getString("name");
                book.setGenre(genre);
            }

            // Assign title and author
            book.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            book.author = getAuthorForBook(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public static Book fromJSONWithOpenLibraryId(JSONObject data) {
        Book book = new Book();

        try {
            // Assign number of pages
            if (data.has("number_of_pages")) {
                book.setPages(data.getInt("number_of_pages"));
            }

            // Assign the OpenLibraryId and ISBN for the book
            if (data.has("identifiers")) {
                JSONObject identifiers = data.getJSONObject("identifiers");

                // Assign OpenLibraryId
                if (identifiers.has("openlibrary")) {
                    JSONArray openLibraryArray = identifiers.getJSONArray("openlibrary");
                    if (openLibraryArray.length() > 0) {
                        book.setOpenLibraryId(openLibraryArray.getString(0));
                    }
                }

                // Assign ISBN
                if (identifiers.has("isbn_10")) {
                    JSONArray isbnArray = identifiers.getJSONArray("isbn_10");
                    if (isbnArray.length() > 0) {
                        book.setISBN(isbnArray.getString(0));
                    }
                }
            }

            // Assign the publisher for the book
            if (data.has("publishers")) {
                JSONArray jsonArray = data.getJSONArray("publishers");
                if (jsonArray.length() > 0) {
                    JSONObject firstPublisher = (JSONObject) jsonArray.get(0);
                    book.setPublisher(firstPublisher.getString("name"));
                }
            }

            // Assign the genre of the book
            if (data.has("subjects")) {
                JSONArray jsonArray = data.getJSONArray("subjects");
                JSONObject firstGenreObject = (JSONObject) jsonArray.get(0);
                book.setGenre(firstGenreObject.getString("name"));
            }

            // Get author or authors and book title for the book
            book.setAuthor(getAuthorForBook(data));
            book.title = data.has("title") ? data.getString("title") : "";
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

    private static String getAuthorForBook(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("authors");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                JSONObject authorObject = authors.getJSONObject(i);
                authorStrings[i] = authorObject.getString("name");
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public void setOpenLibraryId(String id) {
        this.openLibraryId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        return "https://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }
}
