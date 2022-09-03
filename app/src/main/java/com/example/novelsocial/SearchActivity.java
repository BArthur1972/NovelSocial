package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.Adapters.BookAdapter;
import com.example.novelsocial.client.BookClient;
import com.example.novelsocial.models.Book;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
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

        // TODO: Navigate to DetailFragment for a particular book
        adapter.setOnItemClickListener((itemView, position) -> {
            Toast.makeText(getApplicationContext(), "Item at position " + position + " has been clicked", Toast.LENGTH_SHORT).show();
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
                filterList(newText);
                return true;
            }

            private void filterList(String text) {
                List<Book> filteredList = new ArrayList<>();
                for (Book book : allBooks) {
                    if (book.getTitle().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(book);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Your query is not in the List", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.setFilteredList(filteredList);
                }
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            // Search for books
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

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

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
                        for (Book book: books) {
                            allBooks.add(book);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(ProgressBar.GONE);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                progressBar.setVisibility(ProgressBar.GONE);
                Log.e(SearchActivity.class.getSimpleName(),
                        "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }
}