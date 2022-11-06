package com.example.novelsocial.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.novelsocial.Adapters.HomeParentItemAdapter;
import com.example.novelsocial.SearchActivity;
import com.example.novelsocial.clients.BookSubjectClient;
import com.example.novelsocial.databinding.FragmentHomeBinding;
import com.example.novelsocial.models.HomeChildItem;
import com.example.novelsocial.models.HomeParentItem;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    TextView welcomeLabel;
    RecyclerView parentRecyclerView;
    List<HomeParentItem> parentItemList;
    List<HomeChildItem> childItemList;
    HomeParentItemAdapter parentAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        // layout of fragment is stored in a special property called root
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        welcomeLabel = binding.tvNameLabel;
        welcomeLabel.setText(String.format("Welcome %s!", ParseUser.getCurrentUser().get("fullName")));

        parentRecyclerView = binding.rvParentRecyclerView;

        String[] genres = new String[]{"Fantasy", "Adventure", "Love", "Fiction", "History", "Biography", "Poetry"};
        parentItemList = new ArrayList<>();

        // Populate recycler view with books based on genre.
        for (String genre: genres) {
            parentAdapter = new HomeParentItemAdapter(requireContext(), parentItemList);

            // Set adapter on RecyclerView
            parentRecyclerView.setAdapter(parentAdapter);

            // Set layout manager on RecyclerView
            parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            fetchBooks(genre);
        }
    }

    // Make API request to fetch books based on genre
    public void fetchBooks(String genre) {

        // Create BookSubjectClient object and call the getBooks method which makes the API call
        BookSubjectClient client = new BookSubjectClient();
        client.getBooks(genre, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON jsonResponse) {

                try {
                    JSONArray works;
                    if (jsonResponse != null) {
                        // Store JSON array of book objects
                        works = jsonResponse.jsonObject.getJSONArray("works");

                        // Parse each book object and obtain necessary properties(publisher, author, title, etc) for a Book Object
                        final ArrayList<HomeChildItem> books = HomeChildItem.childItemFromJson(works);

                        // Make sure ArrayList is empty
                        childItemList = new ArrayList<>();

                        // Add new Book objects to ArrayList
                        childItemList.addAll(books);

                        // New Parent Item
                        HomeParentItem parentItem = new HomeParentItem(genre, childItemList);
                        parentItemList.add(parentItem);
                        parentAdapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e) {
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