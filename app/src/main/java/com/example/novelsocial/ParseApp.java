package com.example.novelsocial;

import android.app.Application;

import com.example.novelsocial.models.BookComments;
import com.example.novelsocial.models.LibraryItem;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register Subclasses
        ParseObject.registerSubclass(LibraryItem.class);
        ParseObject.registerSubclass(BookComments.class);

        // Connect parse app to dashboard
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
