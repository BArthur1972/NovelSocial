package com.example.novelsocial.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.Adapters.LibraryItemAdapter;
import com.example.novelsocial.BookDetailsActivity;
import com.example.novelsocial.SearchActivity;
import com.example.novelsocial.clients.BookClient;
import com.example.novelsocial.databinding.FragmentLibraryBinding;
import com.example.novelsocial.interfaces.OnItemClickListener;
import com.example.novelsocial.models.Book;
import com.example.novelsocial.models.LibraryItem;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class LibraryFragment extends Fragment {

    FragmentLibraryBinding binding;
    List<LibraryItem> libraryItems;
    LibraryItemAdapter adapter;

    public LibraryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(getLayoutInflater(), container, false);

        // layout of fragment is stored in a special property called root
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize empty list
        libraryItems = new ArrayList<>();

        RecyclerView rvLibrary  = binding.rvLibraryItems;
        adapter = new LibraryItemAdapter(getContext(), libraryItems);

        // Set adapter on RecyclerView
        rvLibrary.setAdapter(adapter);

        // Set layout manager on RecyclerView
        rvLibrary.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ParseQuery<LibraryItem> query = ParseQuery.getQuery(LibraryItem.class);
        query.include("owner");

        query.orderByDescending("createdAt");

        query.findInBackground((objects, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Failed to get items", Toast.LENGTH_SHORT).show();
                Log.e(getClass().getSimpleName(), e.getMessage());
            }

            String userId = ParseUser.getCurrentUser().getObjectId();

            for (LibraryItem item: objects) {
                if (Objects.equals(item.getUser().getObjectId(), userId)) {
                    libraryItems.add(item);
                }
            }

            adapter.notifyDataSetChanged();
        });

        // Set OnItemClickListener on books which will allow you to navigate to the BookDetailsActivity
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                fetchBook(libraryItems.get(position).getBookId());

            }
        });
    }

    public void fetchBook(String bookId) {

        // Create BookSubjectClient object and call the getBooks method which makes the API call
        BookClient client = new BookClient();
        client.getBook(bookId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON jsonResponse) {
                try {
                    JSONObject bookData;
                    if (jsonResponse != null) {
                        // Store JSON object representing a book
                        bookData = jsonResponse.jsonObject.getJSONObject("OLID:" + bookId);

                        // Parse the book JSON object and store the Book Object
                        Book bookObject = Book.fromJSONWithOpenLibraryId(bookData);

                        FragmentActivity activity = requireActivity();
                        Intent intent = new Intent(activity, BookDetailsActivity.class);

                        intent.putExtra("BookObject", Parcels.wrap(bookObject));
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(SearchActivity.class.getSimpleName(),
                        "Request failed with code: " + statusCode + " , Response message: " + response);
            }
        });
    }
}