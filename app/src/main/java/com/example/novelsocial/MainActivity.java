package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import com.example.novelsocial.fragments.HomeFragment;
import com.example.novelsocial.fragments.LibraryFragment;
import com.example.novelsocial.fragments.ProfileFragment;
import com.example.novelsocial.fragments.SearchFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mi_logout){
            goToLoginActivity();
            return true;

        } else if (item.getItemId() == R.id.mi_search_view) {
            // Navigate to Search Fragment when Search MenuItem in the Action bar is selected
            Fragment searchFragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, searchFragment).commit();

            // Set selected item in bottom navigation view to search.
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.search_fragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        ParseUser.logOut();
    }

    private void goToLoginActivity() {
        logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}