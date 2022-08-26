package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.novelsocial.Fragments.HomeFragment;
import com.example.novelsocial.Fragments.LibraryFragment;
import com.example.novelsocial.Fragments.ProfileFragment;
import com.example.novelsocial.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final HomeFragment homeFragment = new HomeFragment();
        final LibraryFragment libraryFragment = new LibraryFragment();
        final ProfileFragment profileFragment = new ProfileFragment();
        final SearchFragment searchFragment = new SearchFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {

            Fragment fragmentToShow;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home_fragment){
                    fragmentToShow = homeFragment;
                }
                else if (id == R.id.library_fragment) {
                    fragmentToShow = libraryFragment;
                }
                else if (id == R.id.search_fragment) {
                    fragmentToShow = searchFragment;
                }
                else if (id == R.id.profile_fragment) {
                    fragmentToShow = profileFragment;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.home_fragment);
    }
}