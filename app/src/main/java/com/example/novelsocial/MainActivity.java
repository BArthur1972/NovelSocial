package com.example.novelsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import com.example.novelsocial.fragments.HomeFragment;
import com.example.novelsocial.fragments.LibraryFragment;
import com.example.novelsocial.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final HomeFragment homeFragment = new HomeFragment();
        final LibraryFragment libraryFragment = new LibraryFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

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
        if(item.getItemId() == R.id.action_logout){
            goToLoginActivity();
            return true;

        } else if (item.getItemId() == R.id.action_search_activity) {
            // Navigate to Search Fragment when Search MenuItem in the Action bar is selected
            goToSearchActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void goToLoginActivity() {
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}