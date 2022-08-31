package com.example.novelsocial.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.Adapters.BookAdapter;
import com.example.novelsocial.R;
import com.example.novelsocial.client.BookClient;
import com.example.novelsocial.models.Book;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class SearchFragment extends Fragment {

    private RecyclerView rvBooks;
    private BookAdapter adapter;
    private ArrayList<Book> allBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is where you set up the views and click listeners
        rvBooks = view.findViewById(R.id.postRecyclerView);

        // Initialize ArrayList
        allBooks = new ArrayList<>();

        // Set adapter on RecyclerView
        adapter = new BookAdapter(requireContext(), allBooks);
        rvBooks.setAdapter(adapter);

        // Set layout manager on RecyclerView
        rvBooks.setLayoutManager(new LinearLayoutManager(requireContext()));

        // TODO: Navigate to DetailFragment for a particular book
//        adapter.setOnItemClickListener((itemView, position) -> {
//            Toast.makeText(requireContext(), "Item at position " + position + " has been clicked", Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.mi_search_view);

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
                return false;
            }
        });
    }

    // Make API request to fetch books using entry in SearchView
    public void fetchBooks(String bookQuery) {
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

                        Log.i(SearchFragment.class.getSimpleName(),
                                "All books have been Added");
                    }
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(SearchFragment.class.getSimpleName(),
                        "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }
}