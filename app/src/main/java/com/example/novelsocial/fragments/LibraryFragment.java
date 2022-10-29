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
import com.example.novelsocial.databinding.FragmentLibraryBinding;
import com.example.novelsocial.models.LibraryItem;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    }
}