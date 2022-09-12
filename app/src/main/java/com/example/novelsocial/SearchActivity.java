package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.Adapters.BookAdapter;
import com.example.novelsocial.client.BookClient;
import com.example.novelsocial.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Headers;

public class SearchActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private ArrayList<Book> allBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Add the back button in the ActionBar to go back a previous page
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // This is where you set up the views and click listeners
        RecyclerView rvBooks = findViewById(R.id.postRecyclerView);

        // Initialize ArrayList
        allBooks = new ArrayList<>();

        // Set adapter on RecyclerView
        adapter = new BookAdapter(SearchActivity.this, allBooks);
        rvBooks.setAdapter(adapter);

        // Set layout manager on RecyclerView
        rvBooks.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        adapter.setOnItemClickListener((itemView, position) -> {
            // Navigate to Book DetailsFragment when a book is clicked
            Intent intent = new Intent(SearchActivity.this, BookDetailsActivity.class);
            // Add book object containing book information to send to Activity
            intent.putExtra("BookObject", Parcels.wrap(allBooks.get(position)));
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Set up search view in ActionBar
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Expand the search view and request focus
        searchItem.expandActionView();
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchView.clearFocus();
                fetchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Make API request to fetch books using entry in SearchView
    public void fetchBooks(String bookQuery) {
        LottieAnimationView progressAnimation = findViewById(R.id.progressAnimation);
        progressAnimation.setVisibility(View.VISIBLE);
        progressAnimation.playAnimation();

        // Create BookClient object and call the getBooks method which makes the API call
        BookClient client = new BookClient();
        client.getBooks(bookQuery, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON jsonResponse) {

                try {
                    JSONArray docs;
                    if (jsonResponse != null) {
                        // Store JSON array of book objects
                        docs = jsonResponse.jsonObject.getJSONArray("docs");

                        // Parse each book object and obtain necessary properties(publisher, author, title, etc) for a Book Object
                        final ArrayList<Book> books = Book.fromJson(docs);

                        // Make sure ArrayList is empty
                        allBooks.clear();

                        // Add new Book objects to ArrayList
                        allBooks.addAll(books);
                    }
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    stopProgressAnimation(progressAnimation);
                    e.printStackTrace();
                }
                stopProgressAnimation(progressAnimation);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                stopProgressAnimation(progressAnimation);
                Log.e(SearchActivity.class.getSimpleName(),
                        "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }

    public void stopProgressAnimation(LottieAnimationView animationView) {
        animationView.cancelAnimation();
        animationView.setVisibility(View.INVISIBLE);
    }
}