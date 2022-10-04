package com.example.novelsocial.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.novelsocial.Adapters.LibraryItemAdapter;
import com.example.novelsocial.R;
import com.example.novelsocial.models.LibraryItem;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    List<LibraryItem> libraryItems;
    LibraryItemAdapter adapter;

    public LibraryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize empty list
        libraryItems = new ArrayList<>();

        RecyclerView rvLibrary  = view.findViewById(R.id.rv_library_items);
        adapter = new LibraryItemAdapter(getContext(), libraryItems);

        // Set adapter on RecyclerView
        rvLibrary.setAdapter(adapter);

        // Set layout manager on RecyclerView
        rvLibrary.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ParseQuery<LibraryItem> query = ParseQuery.getQuery(LibraryItem.class);
        query.findInBackground((objects, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Failed to get items", Toast.LENGTH_SHORT).show();
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
            libraryItems.addAll(objects);
            adapter.notifyDataSetChanged();
        });
    }
}