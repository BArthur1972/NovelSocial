package com.example.novelsocial.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("LibraryItem")
public class LibraryItem extends ParseObject {
    public static final String KEY_BOOK_ID = "bookId";
    public static final String KEY_AUTHOR_NAME = "authorName";
    public static final String KEY_BOOK_TITLE = "bookTitle";
    public static final String KEY_COVER_URL = "coverUrl";
    public static final String KEY_BOOK_OWNER = "owner";

    public LibraryItem(Book book) {
        setAuthorName(book.getAuthor());
        setBookTitle(book.getTitle());
        setCoverUrl(book.getBookCoverUrl());
        setBookId(book.getOpenLibraryId());
    }

    public LibraryItem(){
    }

    public String getAuthorName() {
        return getString(KEY_AUTHOR_NAME);
    }

    public String getBookTitle() {
        return getString(KEY_BOOK_TITLE);
    }

    public String getCoverUrl() {
        return getString(KEY_COVER_URL);
    }

    public void setAuthorName(String name) {
        put(KEY_AUTHOR_NAME, name);
    }

    public void setBookTitle(String title) {
        put(KEY_BOOK_TITLE, title);
    }

    public void setCoverUrl(String imageUrl) {
        put(KEY_COVER_URL, imageUrl);
    }

    public void setBookId(String id) {
        put(KEY_BOOK_ID, id);
    }

    public String getBookId() {
        return getString(KEY_BOOK_ID);
    }

    // Associate each item with a specific user
    public void setOwner(ParseUser user) {
        put(KEY_BOOK_OWNER, user);
    }

    // Get the user who owns this item
    public ParseUser getUser()  {
        return getParseUser(KEY_BOOK_OWNER);
    }
}