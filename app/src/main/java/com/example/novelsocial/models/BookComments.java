package com.example.novelsocial.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@ParseClassName("BookComments")
public class BookComments extends ParseObject {
    public static final String KEY_BOOK_ID = "bookId";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_USER = "user";

    public void setBookId(String openLibraryId) {
        put(KEY_BOOK_ID, openLibraryId);
    }

    public void setComment(String comment) {
        put(KEY_COMMENT, comment);
    }

    public String getComment() {
        return getString(KEY_COMMENT);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getUser() throws ParseException {
        return Objects.requireNonNull(getParseUser(KEY_USER)).fetchIfNeeded().getUsername();
    }

    public String getCreatedAtAsString(){
        SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy h:mm:ss a", Locale.US);
        Date createdAt = getCreatedAt();
        String strDate;
        if (createdAt != null) {
            strDate = parser.format(createdAt);
            return strDate;
        }
        return "";
    }
}
